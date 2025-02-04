package org.example.gogoma.domain.user.repository;

import org.example.gogoma.controller.response.ApplyResponse;
import org.example.gogoma.domain.user.dto.FriendResponse;

import java.util.List;
import java.util.Optional;

public interface UserCustomRepository {

    Optional<ApplyResponse> getApplyInfoById(int id);

    Optional<Integer> findIdByEmail(String email);

    Optional<List<FriendResponse>> findFriendsOrderByTotalDistanceDesc(int userId);
}