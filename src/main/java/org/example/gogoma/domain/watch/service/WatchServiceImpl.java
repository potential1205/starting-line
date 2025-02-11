package org.example.gogoma.domain.watch.service;

import lombok.RequiredArgsConstructor;
import org.example.gogoma.controller.response.MarathonStartInitDataResponse;
import org.example.gogoma.domain.marathon.entity.Marathon;
import org.example.gogoma.domain.marathon.repository.MarathonRepository;
import org.example.gogoma.domain.user.entity.Friend;
import org.example.gogoma.domain.user.entity.User;
import org.example.gogoma.domain.user.repository.FriendRepository;
import org.example.gogoma.domain.user.repository.UserRepository;
import org.example.gogoma.domain.user.service.UserService;
import org.example.gogoma.domain.usermarathon.entity.UserMarathon;
import org.example.gogoma.domain.usermarathon.repository.UserMarathonRepository;
import org.example.gogoma.domain.watch.dto.MarathonReadyDto;
import org.example.gogoma.exception.ExceptionCode;
import org.example.gogoma.exception.type.DbException;
import org.example.gogoma.external.kakao.oauth.KakaoOauthClient;
import org.example.gogoma.external.kakao.oauth.KakaoUserInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WatchServiceImpl implements WatchService {

    private final KakaoOauthClient kakaoOauthClient;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final MarathonRepository marathonRepository;
    private final UserMarathonRepository userMarathonRepository;

    @Override
    public MarathonStartInitDataResponse sendMarathonStartInitData(String accessToken, int marathonId) {

        KakaoUserInfo kakaoUserInfo = kakaoOauthClient.getUserInfo(accessToken);

        User user = userRepository.findByEmail(kakaoUserInfo.getEmail())
                .orElseThrow(() -> new DbException(ExceptionCode.USER_NOT_FOUND));

        Marathon marathon = marathonRepository.findById(marathonId)
                .orElseThrow(() -> new DbException(ExceptionCode.MARATHON_NOT_FOUND));

        UserMarathon userMarathon = userMarathonRepository.findByUserIdAndMarathonId(user.getId(), marathonId)
                .orElseThrow(() -> new DbException(ExceptionCode.USER_MARATHON_NOT_FOUND));

        List<Friend> friendList = friendRepository.findAllByUserId(user.getId());

        return MarathonStartInitDataResponse.of(
                user.getId(), user.getName(), userMarathon.getTargetPace(),
                marathon.getId(), marathon.getTitle(), friendList);
    }

    @Override
    public MarathonReadyDto send(int userId, int marathonId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DbException(ExceptionCode.USER_NOT_FOUND));

        Marathon marathon = marathonRepository.findById(marathonId)
                .orElseThrow(() -> new DbException(ExceptionCode.MARATHON_NOT_FOUND));

        UserMarathon userMarathon = userMarathonRepository.findByUserIdAndMarathonId(user.getId(), marathonId)
                .orElseThrow(() -> new DbException(ExceptionCode.USER_MARATHON_NOT_FOUND));

        List<Friend> friendList = friendRepository.findAllByUserId(user.getId());

        return MarathonReadyDto.of(
                user.getId(), user.getName(), userMarathon.getTargetPace(),
                marathon.getId(), marathon.getTitle(), friendList);
    }
}
