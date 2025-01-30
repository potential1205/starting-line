package org.example.gogoma.domain.marathon.repository;

import org.example.gogoma.domain.marathon.entity.Marathon;
import org.example.gogoma.domain.marathon.enums.MarathonStatus;

import java.util.List;

public interface MarathonCustomRepository {
    List<Marathon> searchMarathonByConitions(
            MarathonStatus marathonStatus,
            String city,
            String year,
            String month,
            String keyword,
            List<String> courseTypeList);
}
