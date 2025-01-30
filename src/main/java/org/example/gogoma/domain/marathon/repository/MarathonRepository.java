package org.example.gogoma.domain.marathon.repository;

import org.example.gogoma.domain.marathon.entity.Marathon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarathonRepository extends JpaRepository<Marathon, Integer> {
}
