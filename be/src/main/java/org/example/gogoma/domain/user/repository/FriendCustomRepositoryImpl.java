package org.example.gogoma.domain.user.repository;

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


    public List<Friend> findAllByUserIdAndMarathonId(int userId, int marathonId) {

        // 1. 사용자의 친구 ID 목록을 가져오기
        List<Integer> friendIds = queryFactory
                .select(friend.friendId)
                .from(friend)
                .where(friend.userId.eq(userId))
                .fetch();

        if (friendIds.isEmpty()) {
            return Collections.emptyList(); // 친구가 없는 경우 빈 리스트 반환
        }

        // 2. 마라톤 참가자 중에서 친구 ID에 포함된 사람만 필터링
        List<Integer> marathonParticipantFriendIds = queryFactory
                .select(userMarathon.userId)
                .from(userMarathon)
                .where(userMarathon.marathonId.eq(marathonId)  // 해당 마라톤 대회 참가자 중
                        .and(userMarathon.userId.in(friendIds)))  // 친구 목록에 포함된 사용자만 필터링
                .fetch();

        if (marathonParticipantFriendIds.isEmpty()) {
            return Collections.emptyList(); // 참가한 친구가 없으면 빈 리스트 반환
        }

        // 3. 최종적으로 친구 엔티티 리스트 반환 (내 친구들 중 마라톤 참가한 친구들만)
        return queryFactory
                .selectFrom(friend)
                .where(friend.friendId.in(marathonParticipantFriendIds) // 마라톤 참가한 친구만 필터링
                        .and(friend.userId.eq(userId)))  // 내 친구 목록만 가져오기
                .fetch();
    }

    @Override
    public void deleteFriendByUserId(int userId) {
        List<Integer> friendIds = queryFactory
                .select(friend.id)
                .from(friend)
                .where(friend.userId.eq(userId).or(friend.friendId.eq(userId)))
                .fetch();

        if (!friendIds.isEmpty())
            queryFactory
                    .delete(friend)
                    .where(friend.id.in(friendIds))
                    .execute();
    }
}

