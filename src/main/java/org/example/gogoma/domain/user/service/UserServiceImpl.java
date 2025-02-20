package org.example.gogoma.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gogoma.controller.request.AddressRequest;
import org.example.gogoma.controller.response.ApplyResponse;
import org.example.gogoma.domain.marathon.entity.Marathon;
import org.example.gogoma.domain.user.dto.*;
import org.example.gogoma.controller.response.UserResponse;
import org.example.gogoma.domain.user.repository.FriendCustomRepository;
import org.example.gogoma.external.firebase.FirebaseNotificationClient;
import org.example.gogoma.external.kakao.oauth.KakaoFriendListResponse;
import org.example.gogoma.external.kakao.oauth.KakaoFriendResponse;
import org.example.gogoma.domain.user.entity.Friend;
import org.example.gogoma.domain.user.entity.User;
import org.example.gogoma.domain.user.repository.FriendRepository;
import org.example.gogoma.domain.user.repository.UserCustomRepository;
import org.example.gogoma.domain.user.repository.UserRepository;
import org.example.gogoma.exception.type.DbException;
import org.example.gogoma.exception.ExceptionCode;
import org.example.gogoma.external.kakao.oauth.KakaoUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserCustomRepository userCustomRepository;
    private final FriendRepository friendRepository;
    private final FriendCustomRepository friendCustomRepository;
    private final FirebaseNotificationClient firebaseNotificationClient;

    @Override
    @Transactional
    public void createUser(CreateUserRequest createUserRequest) {
        userRepository.save(User.of(createUserRequest));
    }

    @Override
    @Transactional
    public void updateUser(KakaoUserInfo kakaoUserInfo) {
        User existingUser = userRepository.findByEmail(kakaoUserInfo.getEmail())
                .orElseThrow(() -> new DbException(ExceptionCode.USER_NOT_FOUND));

        User.updateWhenLogin(existingUser, kakaoUserInfo);

        userRepository.save(existingUser);
    }

    @Override
    public UserResponse getUserById(String email) {

        User user = userRepository.findById(getIdByEmail(email))
                .orElseThrow(() -> new DbException(ExceptionCode.USER_NOT_FOUND));

        return UserResponse.of(user);
    }

    @Override
    @Transactional
    public void deleteUserById(String email) {
        userRepository.deleteById(getIdByEmail(email));
    }

    @Override
    public ApplyResponse getApplyInfoById(String email) {
        return userCustomRepository.getApplyInfoById(getIdByEmail(email))
                .orElseThrow(() -> new DbException(ExceptionCode.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public void updateFriend(int userId, KakaoFriendListResponse kakaoFriendListResponse) {
        for (KakaoFriendResponse kakaoFriendResponse : kakaoFriendListResponse.getFriends()){
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new DbException(ExceptionCode.USER_NOT_FOUND));
            Optional<User> friendOptional = userRepository.findByKakaoId(kakaoFriendResponse.getId());

            if(friendOptional.isPresent()){
                User friend = friendOptional.get();

                if (!friendRepository.existsByUserIdAndFriendId(userId, friend.getId()))
                    friendRepository.save(Friend.of(userId,friend.getId(),friend.getName()));

                if (!friendRepository.existsByUserIdAndFriendId(friend.getId(),userId))
                    friendRepository.save(Friend.of(friend.getId(),userId,user.getName()));

            }
        }
    }

    @Override
    public int getIdByEmail(String email){
        return userCustomRepository.findIdByEmail(email)
                .orElseThrow(() -> new DbException(ExceptionCode.USER_NOT_FOUND));
    }

    @Override
    public List<FriendResponse> getFriendListOrderByTotalDistance(String email) {
        return userCustomRepository.findFriendsOrderByTotalDistanceDesc(getIdByEmail(email))
                .orElseThrow(() -> new DbException(ExceptionCode.USER_NOT_FOUND));
    }

    @Override
    public List<FriendResponse> getUpcomingMarathonFriendList(String email) {
        int userId = getIdByEmail(email);
        Marathon upComingMarathon = userCustomRepository.findUpcomingMarathonForUser(userId)
                .orElseThrow(() -> new DbException((ExceptionCode.MARATHON_NOT_FOUND)));

        return userCustomRepository.findFriendsWhoAppliedForMarathon(userId, upComingMarathon.getId())
                .orElseThrow(() -> new DbException((ExceptionCode.USER_NOT_FOUND)));
    }

    @Override
    public void sendNotificationToFriends(FcmRequest fcmRequest) {
        List<FriendToken> friendTokens = userCustomRepository.findFcmTokensOfFriendsInMarathon(fcmRequest.getUserId(), fcmRequest.getMarathonId())
                .orElseThrow(() -> new DbException(ExceptionCode.USER_NOT_FOUND));

        friendTokens.forEach(token -> firebaseNotificationClient.sendPushNotification(fcmRequest, token.getFcmToken()));
    }

    @Override
    public UserAlertInfo getUserAlertInfoByEmail(String email) {
        return userCustomRepository.findIdAndNameByEmail(email)
                .orElseThrow(() -> new DbException(ExceptionCode.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public void deleteFriend(String email) {
        friendCustomRepository.deleteFriendByUserId(getIdByEmail(email));
    }

    @Override
    @Transactional
    public void updateUserAddress(String email, AddressRequest addressRequest) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DbException(ExceptionCode.USER_NOT_FOUND));
        userCustomRepository.updateAddressById(user.getId(),addressRequest.getRoad_address(),addressRequest.getDetail_address());
    }


}
