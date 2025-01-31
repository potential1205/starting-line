package org.example.gogoma.domain.user.service;

import org.example.gogoma.controller.response.UserListResponse;
import org.example.gogoma.controller.response.ApplyResponse;
import org.example.gogoma.domain.user.dto.CreateUserRequest;
import org.example.gogoma.controller.response.UserResponse;
import org.example.gogoma.domain.user.dto.FriendListResponse;
import org.example.gogoma.external.kakao.oauth.KakaoUserInfo;


public interface UserService {

    void createUser(CreateUserRequest createUserRequest);

    void updateUser(KakaoUserInfo kaKaoUserInfo);

    UserResponse getUserById(String email);

    UserListResponse getAllUsers();

    void deleteUserById(String email);

    ApplyResponse getApplyInfoById(String email);

    void updateFriend(int userId, FriendListResponse friendListResponse);

    int getIdByEmail(String email);
}
