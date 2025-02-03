package org.example.gogoma.domain.user.repository;

import org.example.gogoma.controller.response.ApplyResponse;

import java.util.Optional;

public interface UserCustomRepository {

    Optional<ApplyResponse> getApplyInfoById(int id);

    Optional<Integer> findIdByEmail(String email);
}
