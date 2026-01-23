package com.jichan.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class ContactDto {

    public record SpecialtyInfo(
            String name,
            Integer hourlyRate,
            Long specialtyDetailId // 프론트엔드에서 이름 매핑을 위해 필요할 수 있음
    ) {}

    public record ContactListResponse(
            Long expertId,
            String expertName,
            String gender,
            String region,
            List<SpecialtyInfo> specialties,
            String introduction,
            Boolean hasEmailView,
            Boolean hasPhoneView,
            Integer rating,
            String email,
            String phone,
            String phoneMessage
    ) {}

    public record ContactSliceResponse(
            List<ContactListResponse> content,
            boolean hasNext
    ) {}

    public record RatingRequest(
            @NotNull(message = "전문가 ID는 필수입니다") Long expertId,
            @NotNull(message = "점수는 필수입니다") @Min(value = 1, message = "점수는 1 이상이어야 합니다") @Max(value = 5, message = "점수는 5 이하여야 합니다") Integer score
    ) {}

    public record RatingResponse(
            Long id,
            Long expertId,
            Integer score
    ) {}

    public record RatingStatsDto(Double average, Long count) {}

}
