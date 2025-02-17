package org.example.gogoma.domain.user.repository;

import org.example.gogoma.domain.user.entity.Friend;

import java.util.List;

public interface FriendCustomRepository {
    List<Friend> findAllByUserIdAndMarathonId(int userId, int marathonId);

    void deleteFriendByUserId(int userId);
}
