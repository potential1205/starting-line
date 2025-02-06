package org.example.gogoma.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.gogoma.common.dto.BooleanResponse;
import org.example.gogoma.controller.response.ApplyResponse;
import org.example.gogoma.domain.user.dto.CreateUserRequest;
import org.example.gogoma.controller.response.UserResponse;
import org.example.gogoma.domain.user.dto.FriendResponse;
import org.example.gogoma.domain.user.dto.StatusResponse;
import org.example.gogoma.external.kakao.oauth.KakaoFriendListResponse;
import org.example.gogoma.domain.user.service.UserService;
import org.example.gogoma.external.kakao.oauth.KakaoClientOauthTokenResponse;
import org.example.gogoma.external.kakao.oauth.KakaoOauthClient;
import org.example.gogoma.external.kakao.oauth.KakaoUrlBuilder;
import org.example.gogoma.external.kakao.oauth.KakaoUserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final KakaoOauthClient kakaoOauthClient;
    private final UserService userService;
    private final KakaoUrlBuilder kakaoUrlBuilder;

    /**
     * 카카오 인가코드 받아오기
     * @return callback link
     */
    @GetMapping("/kakao/login")
    @Operation(summary = "카카오 로그인 접근", description = "카카오 인가코드를 받아올 수 있는 URL을 반환합니다.")
    public ResponseEntity<String> redirectToKakaoLogin() {
        return ResponseEntity.ok(kakaoUrlBuilder.buildKakaoAuthUrl());
    }

    /**
     * 카카오 콜백함수
     * @param code ( 인가코드 )
     * @return access_Token, refresh_Token, status ( login, signup )
     */
    @GetMapping("/kakao/callback")
    @Operation(summary = "callback 후 가입 여부 확인", description = "인가코드를 통해 정보를 받아와 회원가입 여부를 판단하고 토큰을 반환합니다.")
    public ResponseEntity<KakaoClientOauthTokenResponse> handleKakaoCallback(@RequestParam("code") String code) {
        KakaoClientOauthTokenResponse kakaoClientOauthTokenResponse = kakaoOauthClient.determineLoginOrSignupWithWeb(code);
        return ResponseEntity.ok(kakaoClientOauthTokenResponse);
    }

    /**
     * 회원가입 판단 여부
     * @header AccessToken
     * @return access_Token, refresh_Token, status ( login, signup )
     */
    @GetMapping("/auth/check")
    @Operation(summary = "토큰을 통해 회원가입 판단 여부 확인", description = "토큰을 통해 회원가입 여부를 판단하고 상태를 반환합니다.")
    public ResponseEntity<StatusResponse> determineLoginOrSignUp(@RequestHeader("Authorization") String accessToken) {
        StatusResponse statusResponse = kakaoOauthClient.determineLoginOrSignup(accessToken);
        return ResponseEntity.ok(statusResponse);
    }

    /**
     * refreshToken으로 AccessToken 갱신
     * @header Authorization ( refresh_token )
     * @return access_Token
     */
    @PostMapping("/kakao/refresh")
    @Operation(summary = "AccessToken 재발급", description = "RefreshToken을 사용하여 AccessToken을 재발급받습니다.")
    public ResponseEntity<KakaoClientOauthTokenResponse> handleKakaoRefresh(@RequestHeader("Authorization") String refreshToken) {
        KakaoClientOauthTokenResponse kakaoClientOauthTokenResponse = kakaoOauthClient.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(kakaoClientOauthTokenResponse);
    }

    /**
     * ID로 User 조회
     * @header Authorization ( access_token )
     * @return Kakao User 정보
     */
    @GetMapping("/kakao/userinfo")
    @Operation(summary = "AccessToken으로 사용자 정보 조회", description = "AccessToken을 사용하여 사용자의 상세 정보를 조회합니다.")
    public ResponseEntity<KakaoUserInfo> getKakaoUserInfo(@RequestHeader("Authorization") String accessToken) {
        KakaoUserInfo userInfo = kakaoOauthClient.getUserInfo(accessToken);
        return ResponseEntity.ok(userInfo);
    }

    /**
     * 친구 목록 불러오기
     * @header Authorization ( access_token )
     * @return 친구 목록
     */
    @GetMapping("/kakao/friendInfo")
    @Operation(summary = "카카오 친구 목록 조회", description = "AccessToken을 통해 카카오 친구 목록을 조회합니다.")
    public ResponseEntity<KakaoFriendListResponse> getKakaoFriendList(@RequestHeader("Authorization") String accessToken) {
        KakaoFriendListResponse kakaoFriendListResponse = kakaoOauthClient.getFriendList(accessToken);
        return ResponseEntity.ok(kakaoFriendListResponse);
    }

    @PostMapping("/update/friend")
    @Operation(summary = "카카오 친구 목록 조회", description = "AccessToken을 통해 카카오 친구 목록을 조회합니다.")
    public ResponseEntity<BooleanResponse> updateFriendList(@RequestHeader("Authorization") String accessToken) {
        userService.updateFriend(userService.getIdByEmail(kakaoOauthClient.getUserInfo(accessToken).getEmail()),kakaoOauthClient.getFriendList(accessToken));
        return ResponseEntity.ok(BooleanResponse.success());
    }
    
    /**
     * 꼬마 서비스 로그인
     * @header Authorization ( access_token )
     * @return 로그인 성공 여부
     */
    @PostMapping("/login")
    @Operation(summary = "서비스 로그인", description = "AccessToken을 통해 DB 사용자 정보와 친구 목록을 갱신하고 서비스를 로그인합니다.")
    public ResponseEntity<BooleanResponse> login(@RequestHeader("Authorization") String accessToken) {
        KakaoUserInfo kakaoUserInfo = kakaoOauthClient.getUserInfo(accessToken);
        userService.updateUser(kakaoUserInfo);
        KakaoFriendListResponse kakaoFriendListResponse = kakaoOauthClient.getFriendList(accessToken);
        userService.updateFriend(userService.getIdByEmail(kakaoUserInfo.getEmail()), kakaoFriendListResponse);
        return ResponseEntity.ok(BooleanResponse.success());
    }

    /**
     * 꼬마 서비스 회원가입
     * @header SignUpRequest
     * @return 회원가입 성공 여부
     */
    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "SignUpRequest를 받아 DB에 사용자 정보를 저장합니다.")
    public ResponseEntity<BooleanResponse> signUp(@RequestBody CreateUserRequest createUserRequest) {
        userService.createUser(createUserRequest);
        return ResponseEntity.ok(BooleanResponse.success());
    }

    /**
     * ID로 User 삭제
     * @param accessToken
     * @return User 삭제 성공 여부
     */
    @DeleteMapping
    @Operation(summary = "회원탈퇴", description = "accessToken을 받아 DB에서 해당 사용자 정보를 삭제합니다.")
    public ResponseEntity<BooleanResponse> deleteUserByID(@RequestHeader("Authorization") String accessToken) {
        userService.deleteUserById(kakaoOauthClient.getUserInfo(accessToken).getEmail());
        return ResponseEntity.ok(BooleanResponse.success());
    }

    /**
     * 신청 시 필요한 정보
     * @Path accessToken
     * @return applyResponse
     */
    @GetMapping("/apply")
    @Operation(summary = "신청 시 필요한 정보", description = "accessToken을 통해 신청 시 필요한 정보를 받아옵니다.")
    public ResponseEntity<ApplyResponse> getApplyInfoById(@RequestHeader("Authorization") String accessToken) {
        ApplyResponse applyResponse = userService.getApplyInfoById(kakaoOauthClient.getUserInfo(accessToken).getEmail());
        return ResponseEntity.ok(applyResponse);
    }

    /**
     * ID로 User 조회
     * @param accessToken
     * @return User 정보
     */
    @GetMapping("/userinfo")
    @Operation(summary = "ID로 사용자 조회", description = "accessToken을 사용하여 사용자의 상세 정보를 조회합니다.")
    public ResponseEntity<UserResponse> getUserById(@RequestHeader("Authorization") String accessToken) {
        UserResponse userResponse = userService.getUserById(kakaoOauthClient.getUserInfo(accessToken).getEmail());
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/friends")
    @Operation(summary = "친구 목록 누적 거리 순으로 조회", description = "accessToken을 사용하여 사용자의 친구 목록을 누적 거리 순으로 조회합니다.")
    public ResponseEntity<List<FriendResponse>> getFriendListOrderByTotalDistance(@RequestHeader("Authorization") String accessToken) {
        List<FriendResponse> friendResponses = userService.getFriendListOrderByTotalDistance(kakaoOauthClient.getUserInfo(accessToken).getEmail());
        return ResponseEntity.ok(friendResponses);
    }

    @GetMapping("/upcoming/friends")
    @Operation(summary = "나에게 가장 가까이 다가온 대회에 신청한 친구 목록 조회", description = "accessToken을 사용하여 내가 신청한 가장 가까운 대회에 신청한 친구 목록을 조회합니다.")
    public ResponseEntity<List<FriendResponse>> getUpcomingMarathonFriendList(@RequestHeader("Authorization") String accessToken) {
        List<FriendResponse> friendResponses = userService.getUpcomingMarathonFriendList(kakaoOauthClient.getUserInfo(accessToken).getEmail());
        return ResponseEntity.ok(friendResponses);
    }
}
