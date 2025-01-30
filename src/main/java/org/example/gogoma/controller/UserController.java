package org.example.gogoma.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.gogoma.common.dto.BooleanResponse;
import org.example.gogoma.controller.response.UserListResponse;
import org.example.gogoma.controller.response.UserResponse;
import org.example.gogoma.domain.user.dto.ApplyResponse;
import org.example.gogoma.domain.user.dto.CreateUserRequest;
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
    @Operation(summary = "카카오 로그인 접근", description = "카카오 인가코드를 받아올 수 있는 URL을 반환합니다.")
    public ResponseEntity<String> redirectToKaKaoLogin() {
        return ResponseEntity.ok(kakaoUrlBuilder.buildKakaoAuthUrl());
    }

    /**
     * 카카오 콜백함수
     * @param code ( 인가코드 )
     * @return access_Token, refresh_Token, status ( login, signup )
     */
    @GetMapping("/kakao/callback")
    @Operation(summary = "callback 후 가입 여부 확인", description = "인가코드를 통해 정보를 받아와 회원가입 여부를 판단하고 토큰을 반환합니다.")
    public ResponseEntity<KakaoClientOauthTokenResponse> handleKaKaoCallback(@RequestParam("code") String code) {
        KakaoClientOauthTokenResponse kakaoClientOauthTokenResponse = kakaoOauthClient.determineLoginOrSignup(code);
        return ResponseEntity.ok(kakaoClientOauthTokenResponse);
    }

    /**
     * refreshToken으로 AccessToken 갱신
     * @header Authorization ( refresh_token )
     * @return access_Token
     */
    @PostMapping("/kakao/refresh")
    @Operation(summary = "AccessToken 재발급", description = "RefreshToken을 사용하여 AccessToken을 재발급받습니다.")
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
    @Operation(summary = "AccessToken으로 사용자 정보 조회", description = "AccessToken을 사용하여 사용자의 상세 정보를 조회합니다.")
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
    @Operation(summary = "서비스 로그인", description = "AccessToken을 통해 DB 사용자 정보를 갱신하고 서비스를 로그인합니다.")
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
    @Operation(summary = "회원가입", description = "SignUpRequest를 받아 DB에 사용자 정보를 저장합니다.")
    public ResponseEntity<BooleanResponse> signUp(@RequestBody CreateUserRequest createUserRequest) {
        userService.createUser(createUserRequest);
        return ResponseEntity.ok(BooleanResponse.success());
    }

    /**
     * ID로 User 삭제
     * @param id 조회할 User의 ID
     * @return User 삭제 성공 여부
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "회원탈퇴", description = "사용자의 ID를 받아 DB에서 해당 사용자 정보를 삭제합니다.")
    public ResponseEntity<BooleanResponse> deleteUserByID(@PathVariable int id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok(BooleanResponse.success());
    }

    /**
     * 신청 시 필요한 정보
     * @Path id
     * @return applyResponse
     */
    @GetMapping("/apply/{id}")
    @Operation(summary = "신청 시 필요한 정보", description = "회원 id를 통해 신청 시 필요한 정보를 받아옵니다.")
    public ResponseEntity<ApplyResponse> getApplyInfoById(@PathVariable("id") int id) {
        ApplyResponse applyResponse = userService.getApplyInfoById(id);
        return ResponseEntity.ok(applyResponse);
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

}
