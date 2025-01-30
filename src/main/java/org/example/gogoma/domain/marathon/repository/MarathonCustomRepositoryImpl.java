package org.example.gogoma.domain.marathon.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.example.gogoma.domain.marathon.entity.Marathon;
import org.example.gogoma.domain.marathon.enums.MarathonStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.example.gogoma.domain.marathon.entity.QMarathon.marathon;
import static org.example.gogoma.domain.marathon.entity.QMarathonType.marathonType;

@Repository
public class MarathonCustomRepositoryImpl implements MarathonCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public MarathonCustomRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Marathon> searchMarathonByConitions(
            MarathonStatus marathonStatus, String city, String year, String month, String keyword, List<String> courseTypeList) {

        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null && !keyword.isBlank()) {
            builder.and(marathon.title.contains(keyword));
        }

        if (year != null && !year.isBlank()) {
            builder.and(marathon.year.contains(year));
        }

        if (month != null && !month.isBlank()) {
            builder.and(marathon.month.contains(month));
        }

        if (city != null && !city.isBlank()) {
            builder.and(marathon.city.contains(city));
        }

        if (marathonStatus != null) {
            builder.and(marathon.marathonStatus.eq(marathonStatus));
        }

        // 특정 종목 포함 여부 확인
        if (courseTypeList != null && !courseTypeList.isEmpty()) {
            builder.and(
                    JPAExpressions.selectOne()
                            .from(marathonType)
                            .where(
                                    marathonType.marathonId.eq(marathon.id),
                                    marathonType.courseType.in(courseTypeList)  // 특정 종목 포함 여부 확인
                            )
                            .exists()
            );
        }

        return jpaQueryFactory
                .selectFrom(marathon)
                .join(marathonType).on(marathon.id.eq(marathonType.marathonId))
                .where(builder)
                .fetch();
    }
}
