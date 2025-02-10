package org.example.gogoma.controller;

import lombok.RequiredArgsConstructor;
import org.example.gogoma.controller.response.UpcomingMarathonInfoResponse;
import org.example.gogoma.controller.response.UserMarathonDetailResponse;
import org.example.gogoma.controller.response.UserMarathonSearchResponse;
import org.example.gogoma.domain.user.entity.User;
import org.example.gogoma.domain.user.repository.UserRepository;
import org.example.gogoma.domain.usermarathon.service.UserMarathonService;
import org.example.gogoma.exception.ExceptionCode;
import org.example.gogoma.exception.type.DbException;
import org.example.gogoma.external.kakao.oauth.KakaoOauthClient;
import org.example.gogoma.external.kakao.oauth.KakaoUserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/usermarathons")
public class UserMarathonController {

    private final UserRepository userRepository;
    private final KakaoOauthClient kakaoOauthClient;
    private final UserMarathonService userMarathonService;

    @GetMapping
    public ResponseEntity<UserMarathonSearchResponse> searchUserMarathonList(
            @RequestHeader("Authorization") String accessToken) {

        UserMarathonSearchResponse userMarathonSearchResponse =
                userMarathonService.searchUserMarathonList(accessToken);

        return ResponseEntity.ok(userMarathonSearchResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserMarathonDetailResponse> getUserMarathonById(
            @RequestHeader("Authorization") String accessToken, @PathVariable int id) {

        UserMarathonDetailResponse userMarathonDetailResponse =
                userMarathonService.getUserMarathonById(accessToken, id);

        return ResponseEntity.ok(userMarathonDetailResponse);
    }

    @GetMapping("/upcoming/{dDay}")
    public ResponseEntity<UpcomingMarathonInfoResponse> getUpcomingMarathonInfo(
            @RequestHeader("Authorization") String accessToken, @PathVariable int dDay) {

        UpcomingMarathonInfoResponse upcomingMarathonInfoResponse =
                userMarathonService.getUpcomingMarathonInfo(accessToken, dDay);

        return ResponseEntity.ok(upcomingMarathonInfoResponse);
    }


}
