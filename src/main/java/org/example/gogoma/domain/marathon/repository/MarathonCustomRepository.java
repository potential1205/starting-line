package org.example.gogoma.domain.marathon.repository;

import org.example.gogoma.domain.marathon.entity.Marathon;
import org.example.gogoma.domain.marathon.enums.MarathonStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MarathonCustomRepository {
    List<Marathon> searchMarathonByConitions(
            MarathonStatus marathonStatus,
            String city,
            String year,
            String month,
            String keyword,
            List<String> courseTypeList);

    Optional<Marathon> findByUpcomingMarathon(int id, LocalDateTime upcomingDateTime);
}
