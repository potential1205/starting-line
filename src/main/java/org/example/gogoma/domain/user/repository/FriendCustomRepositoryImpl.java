package org.example.gogoma.domain.user.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.example.gogoma.domain.user.entity.Friend;
import org.springframework.stereotype.Repository;

import static org.example.gogoma.domain.user.entity.QFriend.friend;
import static org.example.gogoma.domain.usermarathon.entity.QUserMarathon.userMarathon;

import java.util.Collections;
import java.util.List;

@Repository
public class FriendCustomRepositoryImpl implements FriendCustomRepository {

    private final JPAQueryFactory queryFactory;

    public FriendCustomRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }


    @Override
    public List<Friend> findAllByUserIdAndMarathonId(int userId, int marathonId) {
            List<Integer> friendIds = queryFactory
                    .select(friend.friendId)
                    .from(friend)
                    .where(friend.userId.eq(userId))
                    .fetch();

            if (friendIds.isEmpty()) {
                return Collections.emptyList();
            }

            return queryFactory
                    .selectFrom(friend)
                    .where(friend.userId.in(
                            JPAExpressions
                                    .select(userMarathon.userId)
                                    .from(userMarathon)
                                    .where(userMarathon.marathonId.eq(marathonId)
                                            .and(userMarathon.userId.in(friendIds))
                    )))
                    .fetch();
    }

    @Override
    public void deleteFriendByUserId(int userId) {
        queryFactory
                .delete(friend)
                .where(friend.userId.eq(userId).or(friend.friendId.eq(userId)))
                .execute();
    }
}
