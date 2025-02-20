package org.example.gogoma.domain.user.service;

import org.example.gogoma.controller.request.AddressRequest;
import org.example.gogoma.controller.response.ApplyResponse;
import org.example.gogoma.domain.user.dto.CreateUserRequest;
import org.example.gogoma.controller.response.UserResponse;
import org.example.gogoma.domain.user.dto.FcmRequest;
import org.example.gogoma.domain.user.dto.FriendResponse;
import org.example.gogoma.domain.user.dto.UserAlertInfo;
import org.example.gogoma.external.kakao.oauth.KakaoFriendListResponse;
import org.example.gogoma.external.kakao.oauth.KakaoUserInfo;

import java.util.List;


public interface UserService {

    void createUser(CreateUserRequest createUserRequest);

    void updateUser(KakaoUserInfo kaKaoUserInfo);

    UserResponse getUserById(String email);

    void deleteUserById(String email);

    ApplyResponse getApplyInfoById(String email);

    void updateFriend(int userId, KakaoFriendListResponse kakaoFriendListResponse);

    int getIdByEmail(String email);

    List<FriendResponse> getFriendListOrderByTotalDistance(String email);

    List<FriendResponse> getUpcomingMarathonFriendList(String email);

    void sendNotificationToFriends(FcmRequest fcmRequest);

    UserAlertInfo getUserAlertInfoByEmail(String email);

    void deleteFriend(String email);

    void updateUserAddress(String email, AddressRequest addressRequest);
}
