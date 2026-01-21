package com.jichan.repository;

import com.jichan.dto.ProfileDto;
import com.jichan.entity.User;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.jichan.entity.QUser.user;
import static com.jichan.entity.QUserSpecialty.userSpecialty;
import static com.jichan.entity.QSpecialtyDetail.specialtyDetail;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<User> findProfiles(ProfileDto.ProfileRequest profileRequest, Pageable pageable) {
        List<User> content = queryFactory
                .selectFrom(user)
                .where(
                        user.isVisible.isTrue(),
                        specialtyExists(profileRequest.specialty()),
                        categoryExists(profileRequest.category())
                )
                .orderBy(getOrderSpecifier(profileRequest.sortBy()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    private BooleanExpression specialtyExists(Long specialtyId) {
        if (specialtyId == null) {
            return null;
        }
        return JPAExpressions
                .selectOne()
                .from(userSpecialty)
                .where(
                        userSpecialty.userId.eq(user.id),
                        userSpecialty.specialtyDetailId.eq(specialtyId)
                )
                .exists();
    }

    private BooleanExpression categoryExists(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return JPAExpressions
                .selectOne()
                .from(userSpecialty)
                .join(specialtyDetail).on(specialtyDetail.id.eq(userSpecialty.specialtyDetailId))
                .where(
                        userSpecialty.userId.eq(user.id),
                        specialtyDetail.category.id.eq(categoryId)
                )
                .exists();
    }

    private OrderSpecifier<?> getOrderSpecifier(String sortBy) {
        if ("rating".equals(sortBy)) {
            return new OrderSpecifier<>(Order.DESC, user.averageRating);
        } else if ("price".equals(sortBy)) {
            return new OrderSpecifier<>(Order.ASC, user.minHourlyRate);
        }
        return new OrderSpecifier<>(Order.DESC, user.id);
    }
}
