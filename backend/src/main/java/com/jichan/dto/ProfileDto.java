package com.jichan.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public class ProfileDto {

    public record ProfileRequest(
            Long category,
            Long specialty,
            String sortBy,
            String region,
            @RequestParam(defaultValue = "0") int page
    ) {}

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
            Integer hourlyRate,
            Long specialtyDetailId
    ) {}

    public record ProfileItem(
            Long id,
            String name,
            String gender,
            String region,
            List<SpecialtyInfo> specialties,
            String introduction,
            Double averageRating,
            Integer reviewCount,
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
