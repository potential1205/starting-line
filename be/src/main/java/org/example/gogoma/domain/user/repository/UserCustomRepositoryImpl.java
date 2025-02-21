package org.example.gogoma.domain.user.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.example.gogoma.controller.response.ApplyResponse;
import org.example.gogoma.domain.marathon.entity.Marathon;
import static org.example.gogoma.domain.marathon.entity.QMarathon.marathon;
import org.example.gogoma.domain.user.dto.FriendResponse;
import org.example.gogoma.domain.user.dto.FriendToken;
import static org.example.gogoma.domain.user.entity.QFriend.friend;
import static org.example.gogoma.domain.user.entity.QUser.user;
import static org.example.gogoma.domain.usermarathon.entity.QUserMarathon.userMarathon;

import org.example.gogoma.domain.user.dto.UserAlertInfo;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final JPAQueryFactory queryFactory;

    public UserCustomRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<ApplyResponse> getApplyInfoById(int id) {

        ApplyResponse result = queryFactory
                .select(Projections.constructor(ApplyResponse.class,
                        user.name,
                        user.email,
                        user.gender,
                        user.birthYear,
                        user.birthDate.substring(0, 2),
                        user.birthDate.substring(2, 4),
                        user.phoneNumber,
                        user.roadAddress,
                        user.detailAddress,
                        user.clothingSize
                ))
                .from(user)
                .where(user.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Integer> findIdByEmail(String email) {

        return Optional.ofNullable(queryFactory
                .select(user.id)
                .from(user)
                .where(user.email.eq(email))
                .fetchOne());
    }

    @Override
    public Optional<List<FriendResponse>> findFriendsOrderByTotalDistanceDesc(int userId) {

        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(
                        FriendResponse.class,
                        user.id,
                        user.name,
                        user.profileImage,
                        user.totalDistance
                ))
                .from(user)
                .where(
                        user.id.eq(userId)
                                .or(user.id.in(
                                        JPAExpressions
                                                .select(friend.friendId)
                                                .from(friend)
                                                .where(friend.userId.eq(userId))
                                ))
                )
                .orderBy(user.totalDistance.desc())
                .fetch());
    }

    @Override
    public Optional<Marathon> findUpcomingMarathonForUser(int userId) {

        return Optional.ofNullable(queryFactory
                .select(marathon)
                .from(userMarathon)
                .join(marathon).on(marathon.id.eq(userMarathon.marathonId))
                .where(
                        userMarathon.userId.eq(userId)
                                .and(marathon.raceStartTime.after(LocalDateTime.now()))
                )
                .orderBy(marathon.raceStartTime.asc())
                .fetchFirst());
    }

    @Override
    public Optional<List<FriendResponse>> findFriendsWhoAppliedForMarathon(int userId, int marathonId) {

        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(
                        FriendResponse.class,
                        user.id,
                        user.name,
                        user.profileImage,
                        user.totalDistance
                ))
                .from(friend)
                .join(user).on(user.id.eq(friend.friendId))
                .join(userMarathon)
                .on(userMarathon.userId.eq(friend.friendId)
                        .and(userMarathon.marathonId.eq(marathonId)))
                .where(friend.userId.eq(userId))
                .fetch());
    }

    public Optional<List<FriendToken>> findFcmTokensOfFriendsInMarathon(int userId, int marathonId) {

        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(
                        FriendToken.class,
                        user.fcmToken
                ))
                .from(user)
                .where(user.id.in(
                                queryFactory.select(userMarathon.userId)
                                        .from(userMarathon)
                                        .where(userMarathon.marathonId.eq(marathonId)
                                                .and(userMarathon.userId.in(
                                                        queryFactory.select(friend.friendId)
                                                                .from(friend)
                                                                .where(friend.userId.eq(userId))
                                                ))
                                        )
                        )
                )
                .fetch());
    }

    @Override
    public Optional<UserAlertInfo> findIdAndNameByEmail(String email) {
        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(
                                UserAlertInfo.class, user.id, user.name))
                .from(user)
                .where(user.email.eq(email))
                .fetchOne());
    }

    @Override
    public void updateUserApplyInfoById(int id, String roadAddress, String detailAddress, String clothingSize) {
        queryFactory
                .update(user)
                .set(user.roadAddress, roadAddress)
                .set(user.detailAddress, detailAddress)
                .set(user.clothingSize, clothingSize)
                .where(user.id.eq(id))
                .execute();
    }

}
