package org.example.gogoma.domain.user.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.example.gogoma.controller.response.ApplyResponse;
import org.example.gogoma.domain.user.entity.QUser;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final JPAQueryFactory queryFactory;

    public UserCustomRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<ApplyResponse> getApplyInfoById(int id) {
        QUser user = QUser.user;

        ApplyResponse result = queryFactory
                .select(Projections.constructor(ApplyResponse.class,
                        user.name,
                        user.email,
                        user.gender,
                        user.birthYear,
                        user.birthDate.substring(0, 2),
                        user.birthDate.substring(2, 4),
                        user.phoneNumber,
                        user.roadAddress,
                        user.detailAddress,
                        user.clothingSize
                ))
                .from(user)
                .where(user.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
