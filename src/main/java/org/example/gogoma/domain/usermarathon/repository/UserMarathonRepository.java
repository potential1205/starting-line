package org.example.gogoma.domain.usermarathon.repository;

import org.example.gogoma.domain.usermarathon.entity.UserMarathon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserMarathonRepository extends JpaRepository<UserMarathon, Integer> {
    List<UserMarathon> findAllByUserId(int id);
}
