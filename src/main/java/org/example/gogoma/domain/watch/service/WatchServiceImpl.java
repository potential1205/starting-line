package org.example.gogoma.domain.watch.service;

import lombok.RequiredArgsConstructor;
import org.example.gogoma.controller.request.MarathonEndInitDataRequest;
import org.example.gogoma.controller.response.MarathonStartInitDataResponse;
import org.example.gogoma.domain.marathon.entity.Marathon;
import org.example.gogoma.domain.marathon.entity.MarathonType;
import org.example.gogoma.domain.marathon.repository.MarathonRepository;
import org.example.gogoma.domain.marathon.repository.MarathonTypeRepository;
import org.example.gogoma.domain.user.entity.Friend;
import org.example.gogoma.domain.user.entity.User;
import org.example.gogoma.domain.user.repository.FriendCustomRepository;
import org.example.gogoma.domain.user.repository.UserRepository;
import org.example.gogoma.domain.usermarathon.entity.UserMarathon;
import org.example.gogoma.domain.usermarathon.repository.UserMarathonRepository;
import org.example.gogoma.domain.watch.dto.MarathonReadyDto;
import org.example.gogoma.exception.ExceptionCode;
import org.example.gogoma.exception.type.DbException;
import org.example.gogoma.external.kakao.oauth.KakaoOauthClient;
import org.example.gogoma.external.kakao.oauth.KakaoUserInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WatchServiceImpl implements WatchService {

    private final KakaoOauthClient kakaoOauthClient;
    private final UserRepository userRepository;
    private final FriendCustomRepository friendCustomRepository;
    private final MarathonRepository marathonRepository;
    private final UserMarathonRepository userMarathonRepository;
    private final MarathonTypeRepository marathonTypeRepository;

    @Override
    public MarathonStartInitDataResponse sendMarathonStartInitData(String accessToken, int marathonId) {

        KakaoUserInfo kakaoUserInfo = kakaoOauthClient.getUserInfo(accessToken);

        User user = userRepository.findByEmail(kakaoUserInfo.getEmail())
                .orElseThrow(() -> new DbException(ExceptionCode.USER_NOT_FOUND));

        Marathon marathon = marathonRepository.findById(marathonId)
                .orElseThrow(() -> new DbException(ExceptionCode.MARATHON_NOT_FOUND));

        UserMarathon userMarathon = userMarathonRepository.findByUserIdAndMarathonId(user.getId(), marathonId)
                .orElseThrow(() -> new DbException(ExceptionCode.USER_MARATHON_NOT_FOUND));

        List<Friend> friendList = friendCustomRepository.findAllByUserIdAndMarathonId(user.getId(), marathon.getId());

        MarathonType marathonType = marathonTypeRepository.findById(userMarathon.getMarathonTypeId())
                .orElseThrow(() -> new DbException(ExceptionCode.MARATHON_TYPE_NOT_FOUND));

        return MarathonStartInitDataResponse.of(
                user.getId(), user.getName(), userMarathon.getTargetPace(),
                marathon.getId(), marathon.getTitle(), friendList, marathon.getRaceStartTime(), marathonType.getCourseType());
    }

    @Override
    public MarathonReadyDto send(int userId, int marathonId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DbException(ExceptionCode.USER_NOT_FOUND));

        Marathon marathon = marathonRepository.findById(marathonId)
                .orElseThrow(() -> new DbException(ExceptionCode.MARATHON_NOT_FOUND));

        UserMarathon userMarathon = userMarathonRepository.findByUserIdAndMarathonId(user.getId(), marathonId)
                .orElseThrow(() -> new DbException(ExceptionCode.USER_MARATHON_NOT_FOUND));

        List<Friend> friendList = friendCustomRepository.findAllByUserIdAndMarathonId(user.getId(), marathon.getId());

        return MarathonReadyDto.of(
                user.getId(), user.getName(), userMarathon.getTargetPace(),
                marathon.getId(), marathon.getTitle(), friendList, marathon.getRaceStartTime());
    }

    @Override
    public void updateMarathonEndData(String accessToken, int marathonId, MarathonEndInitDataRequest marathonEndInitDataRequest) {
        KakaoUserInfo kakaoUserInfo = kakaoOauthClient.getUserInfo(accessToken);

        User user = userRepository.findByEmail(kakaoUserInfo.getEmail())
                .orElseThrow(() -> new DbException(ExceptionCode.USER_NOT_FOUND));

        UserMarathon userMarathon = userMarathonRepository.findByUserIdAndMarathonId(user.getId(), marathonId)
                .orElseThrow(() -> new DbException(ExceptionCode.USER_MARATHON_NOT_FOUND));

        userMarathon.updateMarathonEndData(
                marathonEndInitDataRequest.getCurrentPace(),
                marathonEndInitDataRequest.getRunningTime(),
                marathonEndInitDataRequest.getTotalMemberCount(),
                marathonEndInitDataRequest.getMyRank()
        );

        userMarathonRepository.save(userMarathon);
    }
}
