package org.example.gogoma.domain.user.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.example.gogoma.controller.response.ApplyResponse;
import org.example.gogoma.domain.marathon.entity.Marathon;
import org.example.gogoma.domain.marathon.entity.QMarathon;
import org.example.gogoma.domain.user.dto.FriendResponse;
import org.example.gogoma.domain.user.entity.QFriend;
import org.example.gogoma.domain.user.entity.QUser;
import org.example.gogoma.domain.usermarathon.entity.QUserMarathon;
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
        QUser user = QUser.user;

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
        QUser user = QUser.user;

        Integer userId = queryFactory
                .select(user.id)
                .from(user)
                .where(user.email.eq(email))
                .fetchOne();

        return Optional.ofNullable(userId);
    }

    @Override
    public Optional<List<FriendResponse>> findFriendsOrderByTotalDistanceDesc(int userId) {
        QUser user = QUser.user;
        QFriend friend = QFriend.friend;

        List<FriendResponse> result = queryFactory
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
                .fetch();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Marathon> findUpcomingMarathonForUser(int userId) {
        QUserMarathon userMarathon = QUserMarathon.userMarathon;
        QMarathon marathon = QMarathon.marathon;

        Marathon upcomingMarathon = queryFactory
                .select(marathon)
                .from(userMarathon)
                .join(marathon).on(marathon.id.eq(userMarathon.marathonId))
                .where(
                        userMarathon.userId.eq(userId)
                                .and(marathon.raceStartTime.after(LocalDateTime.now()))
                )
                .orderBy(marathon.raceStartTime.asc())
                .fetchFirst();

        return Optional.ofNullable(upcomingMarathon);
    }

    @Override
    public Optional<List<FriendResponse>> findFriendsWhoAppliedForMarathon(int userId, int marathonId) {
        QFriend friend = QFriend.friend;
        QUser user = QUser.user;
        QUserMarathon userMarathon = QUserMarathon.userMarathon;

        List<FriendResponse> result = queryFactory
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
                .fetch();

        return Optional.ofNullable(result);
    }

}
