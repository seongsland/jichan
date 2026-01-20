package com.jichan.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ProfileDto {

    public record ContactViewRequest(
            @NotNull(message = "전문가 ID는 필수입니다") Long expertId,
            @NotNull(message = "연락처 타입은 필수입니다") String contactType
    ) {}

    public record ContactViewResponse(
            String contact,
            String phoneMessage
    ) {}

    public record SpecialtyInfo(
            String name,
            Integer hourlyRate
    ) {}

    public record ProfileItem(
            Long id,
            String name,
            String gender,
            String region,
            List<SpecialtyInfo> specialties,
            String introduction,
            Double averageRating,
            Integer viewCount,
            boolean isEmailViewed,
            boolean isPhoneViewed,
            String email,
            String phone,
            String phoneMessage
    ) {}

    public record ProfileListResponse(
            List<ProfileItem> content,
            Boolean hasNext
    ) {}
}
