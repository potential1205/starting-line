package org.example.gogoma.domain.user.repository;

import org.example.gogoma.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {}
