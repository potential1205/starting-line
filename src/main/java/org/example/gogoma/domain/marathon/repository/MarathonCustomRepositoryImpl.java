package org.example.gogoma.domain.marathon.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import static org.example.gogoma.domain.marathon.entity.QMarathon.marathon;
import static org.example.gogoma.domain.usermarathon.entity.QUserMarathon.userMarathon;

import org.example.gogoma.domain.marathon.entity.Marathon;
import org.example.gogoma.domain.marathon.enums.MarathonStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<Marathon> findByUpcomingMarathon(int userId, LocalDateTime upcomingDateTime) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(marathon)
                .join(userMarathon).on(userMarathon.marathonId.eq(marathon.id))
                .where(userMarathon.userId.eq(userId)
                        .and(marathon.raceStartTime.year().eq(upcomingDateTime.getYear()))
                        .and(marathon.raceStartTime.month().eq(upcomingDateTime.getMonthValue()))
                        .and(marathon.raceStartTime.dayOfMonth().eq(upcomingDateTime.getDayOfMonth())))
                .fetchOne());
    }

    @Override
    public Optional<String> findMarathonNameById(int id) {
        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.constructor(
                        String.class, marathon.title))
                .from(marathon)
                .where(marathon.id.eq(id))
                .fetchOne());
    }
}
