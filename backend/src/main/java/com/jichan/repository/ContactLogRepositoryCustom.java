package com.jichan.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ContactLogRepositoryCustom {
    Slice<Long> findExpertIdsByViewerIdAndFilters(Long viewerId, Long categoryId, Long specialtyDetailId,
                                                  Pageable pageable);
}
