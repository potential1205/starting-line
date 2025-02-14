package org.example.gogoma.domain.usermarathon.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.gogoma.controller.request.CreateUserMarathonRequest;
import org.example.gogoma.controller.response.UserMarathonDetailResponse;
import org.example.gogoma.controller.response.UserMarathonSearchResponse;
import org.example.gogoma.domain.marathon.entity.Marathon;
import org.example.gogoma.domain.marathon.entity.MarathonType;
import org.example.gogoma.domain.marathon.repository.MarathonRepository;
import org.example.gogoma.domain.marathon.repository.MarathonTypeRepository;
import org.example.gogoma.domain.user.entity.User;
import org.example.gogoma.domain.user.repository.UserRepository;
import org.example.gogoma.domain.usermarathon.dto.UserMarathonDetailDto;
import org.example.gogoma.domain.usermarathon.dto.UserMarathonSearchDto;
import org.example.gogoma.domain.usermarathon.entity.UserMarathon;
import org.example.gogoma.domain.usermarathon.repository.UserMarathonRepository;
import org.example.gogoma.exception.ExceptionCode;
import org.example.gogoma.exception.type.BusinessException;
import org.example.gogoma.exception.type.DbException;
import org.example.gogoma.external.kakao.oauth.KakaoOauthClient;
import org.example.gogoma.external.kakao.oauth.KakaoUserInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserMarathonServiceImpl implements UserMarathonService {

    private final UserMarathonRepository userMarathonRepository;
    private final UserRepository userRepository;
    private final KakaoOauthClient kakaoOauthClient;
    private final MarathonTypeRepository marathonTypeRepository;
    private final MarathonRepository marathonRepository;

    @Override
    public UserMarathonSearchResponse searchUserMarathonList(String accessToken) {

        KakaoUserInfo kakaoUserInfo = kakaoOauthClient.getUserInfo(accessToken);

        User user = userRepository.findByEmail(kakaoUserInfo.getEmail())
                .orElseThrow(() -> new DbException(ExceptionCode.USER_NOT_FOUND));

        List<UserMarathon> userMarathonList = userMarathonRepository.findAllByUserId(user.getId());

        List<UserMarathonSearchDto> userMarathonSearchDtoList = userMarathonList.stream()
                .map(userMarathon -> {
                    Marathon marathon = marathonRepository.findById(userMarathon.getMarathonId())
                            .orElseThrow(() -> new DbException(ExceptionCode.MARATHON_NOT_FOUND));

                    MarathonType marathonType = marathonTypeRepository.findById(userMarathon.getMarathonTypeId())
                            .orElseThrow(() -> new DbException(ExceptionCode.MARATHON_TYPE_NOT_FOUND));

                    return UserMarathonSearchDto.of(userMarathon.getId() ,marathon.getTitle(), marathonType.getCourseType(),
                            calculateDDay(marathon.getRaceStartTime()), marathon.getRaceStartTime(), userMarathon.getPaymentDateTime());
                })
                .toList();

        return UserMarathonSearchResponse.of(userMarathonSearchDtoList);
    }

    @Override
    public UserMarathonDetailResponse getUserMarathonById(String accessToken, int userMarathonId) {
        KakaoUserInfo kakaoUserInfo = kakaoOauthClient.getUserInfo(accessToken);

        if (!userRepository.existsByEmail(kakaoUserInfo.getEmail())) {
            throw new DbException(ExceptionCode.USER_NOT_FOUND);
        }

        UserMarathon userMarathon = userMarathonRepository.findById(userMarathonId)
                .orElseThrow(() -> new DbException(ExceptionCode.USER_MARATHON_NOT_FOUND));

        Marathon marathon = marathonRepository.findById(userMarathon.getMarathonId())
                .orElseThrow(() -> new DbException(ExceptionCode.MARATHON_NOT_FOUND));

        List<String> courseTypeList = marathonTypeRepository.findAllByMarathonId(marathon.getId())
                .stream()
                .map(MarathonType::getCourseType)
                .collect(Collectors.toSet())
                .stream()
                .sorted()
                .toList();

        MarathonType marathonType = marathonTypeRepository.findById(userMarathon.getMarathonTypeId())
                .orElseThrow(() -> new DbException(ExceptionCode.MARATHON_TYPE_NOT_FOUND));

        UserMarathonDetailDto userMarathonDetailDto = UserMarathonDetailDto.builder()
                .marathon(marathon)
                .courseTypeList(courseTypeList)
                .paymentDateTime(userMarathon.getPaymentDateTime())
                .paymentAmount(userMarathon.getPaymentAmount())
                .paymentType(userMarathon.getPaymentType())
                .address(userMarathon.getAddress())
                .selectedCourseType(marathonType.getCourseType())
                .targetPace(userMarathon.getTargetPace())
                .build();

        return UserMarathonDetailResponse.of(userMarathonDetailDto);
    }

    @Override
    @Transactional
    public void updateUserMarathon(String accessToken, int marathonId, int targetPace) {
        KakaoUserInfo kakaoUserInfo = kakaoOauthClient.getUserInfo(accessToken);

        User user = userRepository.findByEmail(kakaoUserInfo.getEmail())
                .orElseThrow(() -> new DbException(ExceptionCode.USER_NOT_FOUND));

        Marathon marathon = marathonRepository.findById(marathonId)
                .orElseThrow(() -> new DbException(ExceptionCode.MARATHON_NOT_FOUND));

        UserMarathon userMarathon = userMarathonRepository.findByUserIdAndMarathonId(user.getId(), marathon.getId())
                .orElseThrow(() -> new DbException(ExceptionCode.USER_MARATHON_NOT_FOUND));

        userMarathon.updateTargetPace(targetPace);
    }

    @Override
    public void checkDuplicateUserMarathon(String accessToken, int marathonId) {
        KakaoUserInfo kakaoUserInfo = kakaoOauthClient.getUserInfo(accessToken);

        User user = userRepository.findByEmail(kakaoUserInfo.getEmail())
                .orElseThrow(() -> new DbException(ExceptionCode.USER_NOT_FOUND));

        Marathon marathon = marathonRepository.findById(marathonId)
                .orElseThrow(() -> new DbException(ExceptionCode.MARATHON_NOT_FOUND));

        if (userMarathonRepository.existsByUserIdAndMarathonId(user.getId(), marathon.getId())) {
            throw new BusinessException(ExceptionCode.USER_MARATHON_ALREADY_EXISTS);
        }
    }

    @Override
    @Transactional
    public void createUserMarathon(String accessToken, CreateUserMarathonRequest createUserMarathonRequest) {
        KakaoUserInfo kakaoUserInfo = kakaoOauthClient.getUserInfo(accessToken);

        User user = userRepository.findByEmail(kakaoUserInfo.getEmail())
                .orElseThrow(() -> new DbException(ExceptionCode.USER_NOT_FOUND));

        Marathon marathon = marathonRepository.findById(createUserMarathonRequest.getMarathonId())
                .orElseThrow(() -> new DbException(ExceptionCode.MARATHON_NOT_FOUND));

        MarathonType marathonType = marathonTypeRepository
                .findByMarathonIdAndCourseType(marathon.getId(), createUserMarathonRequest.getCourseType())
                .orElseThrow(() -> new DbException(ExceptionCode.MARATHON_TYPE_NOT_FOUND));

        UserMarathon userMarathon = UserMarathon.builder()
                .userId(user.getId())
                .marathonId(marathon.getId())
                .marathonTypeId(marathonType.getId())
                .address(createUserMarathonRequest.getAddress())
                .paymentType(createUserMarathonRequest.getPaymentType())
                .paymentAmount(createUserMarathonRequest.getPaymentAmount())
                .paymentDateTime(createUserMarathonRequest.getPaymentDateTime())
                .build();

        userMarathonRepository.save(userMarathon);
    }

    private String calculateDDay(LocalDateTime raceStartTime) {
        if (raceStartTime == null) {
            return "D-?";
        }
        long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), raceStartTime.toLocalDate());
        return daysBetween >= 0 ? "D-" + daysBetween : "D+" + Math.abs(daysBetween);
    }
}
