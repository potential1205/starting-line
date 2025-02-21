package org.example.gogoma.domain.marathon.repository;

import org.example.gogoma.domain.marathon.entity.MarathonType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarathonTypeRepository extends JpaRepository<MarathonType, Integer> {
    List<MarathonType> findAllByMarathonId(int id);

    void deleteAllByMarathonId(int id);

    Optional<MarathonType> findByMarathonIdAndCourseType(int id, int courseType);
}
