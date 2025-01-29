package org.example.gogoma.domain.marathon.repository;

import org.example.gogoma.domain.marathon.entity.MarathonType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarathonTypeRepository extends JpaRepository<MarathonType, Integer> {
    List<MarathonType> findAllByMarathonId(int id);

    void deleteAllByMarathonId(int id);
}
