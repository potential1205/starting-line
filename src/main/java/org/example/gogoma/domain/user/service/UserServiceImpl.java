package org.example.gogoma.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gogoma.controller.response.UserListResponse;
import org.example.gogoma.controller.response.ApplyResponse;
import org.example.gogoma.domain.user.dto.CreateUserRequest;
import org.example.gogoma.controller.response.UserResponse;
import org.example.gogoma.domain.user.dto.FriendListResponse;
import org.example.gogoma.domain.user.dto.FriendResponse;
import org.example.gogoma.domain.user.entity.Friend;
import org.example.gogoma.domain.user.entity.User;
import org.example.gogoma.domain.user.repository.FriendRepository;
import org.example.gogoma.domain.user.repository.UserCustomRepository;
import org.example.gogoma.domain.user.repository.UserRepository;
import org.example.gogoma.exception.type.DbException;
import org.example.gogoma.exception.ExceptionCode;
import org.example.gogoma.external.kakao.oauth.KakaoUserInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserCustomRepository userCustomRepository;
    private final FriendRepository friendRepository;

    @Override
    public void createUser(CreateUserRequest createUserRequest) {
        userRepository.save(User.of(createUserRequest));
    }

    @Override
    public void updateUser(KakaoUserInfo kakaoUserInfo) {
        User existingUser = userRepository.findByEmail(kakaoUserInfo.getEmail())
                .orElseThrow(() -> new DbException(ExceptionCode.USER_NOT_FOUND));

        User updatedUser = User.updateWhenLogin(existingUser, kakaoUserInfo);

        userRepository.save(updatedUser);
    }

    @Override
    public UserResponse getUserById(String email) {

        User user = userRepository.findById(getIdByEmail(email))
                .orElseThrow(() -> new DbException(ExceptionCode.USER_NOT_FOUND));

        return UserResponse.of(user);
    }

    @Override
    public UserListResponse getAllUsers() {

        List<User> userList = userRepository.findAll();

        return UserListResponse.of(userList);
    }

    @Override
    public void deleteUserById(String email) {
        userRepository.deleteById(getIdByEmail(email));
    }

    @Override
    public ApplyResponse getApplyInfoById(String email) {
        return userCustomRepository.getApplyInfoById(getIdByEmail(email))
                .orElseThrow(() -> new DbException(ExceptionCode.USER_NOT_FOUND));
    }

    @Override
    public int getIdByEmail(String email){
        return userRepository.findIdByEmail(email)
                .orElseThrow(() -> new DbException(ExceptionCode.USER_NOT_FOUND));
    }

    @Override
    public void updateFriend(int userId, FriendListResponse friendListResponse) {
        for (FriendResponse friendResponse: friendListResponse.getFriends()){
            Optional<User> friendOptional = userRepository.findByKakaoId(friendResponse.getId());

            if(friendOptional.isPresent()){
                User friend = friendOptional.get();

                if (!friendRepository.existsByUserIdAndFriendId(userId, friend.getId()))
                    friendRepository.save(Friend.of(userId,friend.getId()));

                if (!friendRepository.existsByUserIdAndFriendId(friend.getId(),userId))
                    friendRepository.save(Friend.of(friend.getId(),userId));

            }
        }
    }

}
