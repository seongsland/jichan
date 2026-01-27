package com.jichan.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.jichan.entity.QContactLog.contactLog;
import static com.jichan.entity.QSpecialtyDetail.specialtyDetail;
import static com.jichan.entity.QUser.user;
import static com.jichan.entity.QUserSpecialty.userSpecialty;

@RequiredArgsConstructor
public class ContactLogRepositoryImpl implements ContactLogRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Long> findExpertIdsByViewerIdAndFilters(Long viewerId, Long categoryId, Long specialtyDetailId,
                                                         Pageable pageable) {
        List<Long> content = queryFactory.select(contactLog.expertId).from(contactLog)
                                         .where(contactLog.viewerId.eq(viewerId), visibleUserExists(),
                                                 specialtyExists(specialtyDetailId),
                                                 categoryExists(categoryId, specialtyDetailId)).distinct()
                                         .offset(pageable.getOffset()).limit(pageable.getPageSize() + 1).fetch();

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    private BooleanExpression visibleUserExists() {
        return JPAExpressions.selectOne().from(user).where(user.id.eq(contactLog.expertId), user.isVisible.eq(true))
                             .exists();
    }

    private BooleanExpression specialtyExists(Long specialtyId) {
        if (specialtyId == null) {
            return null;
        }
        return JPAExpressions.selectOne().from(userSpecialty).where(userSpecialty.userId.eq(contactLog.expertId),
                userSpecialty.specialtyDetailId.eq(specialtyId)).exists();
    }

    private BooleanExpression categoryExists(Long categoryId, Long specialtyDetailId) {
        if (categoryId == null || specialtyDetailId != null) {
            return null;
        }
        return JPAExpressions.selectOne().from(userSpecialty).join(specialtyDetail)
                             .on(specialtyDetail.id.eq(userSpecialty.specialtyDetailId))
                             .where(userSpecialty.userId.eq(contactLog.expertId),
                                     specialtyDetail.category.id.eq(categoryId)).exists();
    }
}
