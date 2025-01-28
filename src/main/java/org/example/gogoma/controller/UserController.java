package org.example.gogoma.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.gogoma.common.dto.BooleanResponse;
import org.example.gogoma.controller.response.UserListResponse;
import org.example.gogoma.controller.response.UserResponse;
import org.example.gogoma.domain.user.dto.SignUpRequest;
import org.example.gogoma.domain.user.service.UserService;
import org.example.gogoma.external.kakao.oauth.KakaoClientOauthTokenResponse;
import org.example.gogoma.external.kakao.oauth.KakaoOauthClient;
import org.example.gogoma.external.kakao.oauth.KakaoUrlBuilder;
import org.example.gogoma.external.kakao.oauth.KakaoUserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> redirectToKaKaoLogin() {
        return ResponseEntity.ok(kakaoUrlBuilder.buildKakaoAuthUrl());
    }

    /**
     * 카카오 콜백함수
     * @param code ( 인가코드 )
     * @return access_Token, refresh_Token, status ( login, signup )
     */
    @GetMapping("/kakao/callback")
    public ResponseEntity<KakaoClientOauthTokenResponse> handleKaKaoCallback(@RequestParam("code") String code) {
        KakaoClientOauthTokenResponse kakaoClientOauthTokenResponse = kakaoOauthClient.determineLoginOrSignup(code);
        return ResponseEntity.ok(kakaoClientOauthTokenResponse);
    }

    /**
     * refreshToken으로 AccessToken 갱신
     * @header Authorization ( refresh_token )
     * @return access_Token, refresh_Token
     */
    @PostMapping("/kakao/refresh")
    public ResponseEntity<KakaoClientOauthTokenResponse> handleKaKaoRefresh(@RequestHeader("Authorization") String refreshToken) {
        KakaoClientOauthTokenResponse kakaoClientOauthTokenResponse = kakaoOauthClient.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(kakaoClientOauthTokenResponse);
    }

    /**
     * ID로 User 조회
     * @header Authorization ( access_token )
     * @return Kakao User 정보
     */
    @GetMapping("/kakao/userinfo")
    public ResponseEntity<KakaoUserInfo> getKaKaoUserInfo(@RequestHeader("Authorization") String accessToken) {
        KakaoUserInfo userInfo = kakaoOauthClient.getUserInfo(accessToken);
        return ResponseEntity.ok(userInfo);
    }

    /**
     * 꼬마 서비스 로그인
     * @header Authorization ( access_token )
     * @return 로그인 성공 여부
     */
    @PostMapping("/login")
    public ResponseEntity<BooleanResponse> login(@RequestHeader("Authorization") String accessToken) {
        KakaoUserInfo kakaoUserInfo = kakaoOauthClient.getUserInfo(accessToken);
        userService.updateUser(kakaoUserInfo);
        return ResponseEntity.ok(BooleanResponse.success());
    }

    /**
     * 꼬마 서비스 회원가입
     * @header SignUpRequest
     * @return 회원가입 성공 여부
     */
    @PostMapping("/signup")
    public ResponseEntity<BooleanResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        userService.createUser(signUpRequest);
        return ResponseEntity.ok(BooleanResponse.success());
    }

    /**
     * ID로 User 조회
     * @param id 조회할 User의 ID
     * @return User 정보
     */
    @GetMapping("/{id}")
    @Operation(summary = "ID로 사용자 조회", description = "고유 ID를 사용하여 사용자의 상세 정보를 조회합니다.")
    public ResponseEntity<UserResponse> getUserById(@PathVariable int id) {

        UserResponse userResponse = userService.getUserById(id);

        return ResponseEntity.ok(userResponse);
    }

    /**
     * 전체 User 조회
     * @return 유저 목록
     */
    @GetMapping("")
    @Operation(summary = "모든 사용자 조회", description = "시스템에 등록된 모든 사용자의 목록을 조회합니다. 별도의 매개변수 없이 호출할 수 있습니다.")
    public ResponseEntity<UserListResponse> getAllUsers() {

        UserListResponse userListResponse = userService.getAllUsers();

        return ResponseEntity.ok(userListResponse);
    }

    /**
     * ID로 User 삭제
     * @param id 조회할 User의 ID
     * @return User 삭제 성공 여부
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "ID로 사용자 삭제", description = "고유 ID를 사용하여 사용자를 삭제합니다. 삭제 성공 여부를 반환합니다.")
    public ResponseEntity<BooleanResponse> deleteUserById(@PathVariable int id) {

        userService.deleteUserById(id);

        return ResponseEntity.ok(
                BooleanResponse.success()
        );
    }
}
