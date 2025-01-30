package org.example.gogoma.domain.user.repository;

import org.example.gogoma.domain.user.dto.ApplyResponse;

import java.util.Optional;

public interface UserCustomRepository {

    Optional<ApplyResponse> getApplyInfoById(int id);
}
