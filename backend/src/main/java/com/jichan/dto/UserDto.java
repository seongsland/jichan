package com.jichan.dto;

import java.util.List;

public class UserDto {

    public record SpecialtyRequest(
            Long specialtyDetailId,
            Integer hourlyRate
    ) {}

    public record SpecialtyResponse(
            Long specialtyDetailId,
            String specialtyDetailName,
            String specialtyCategoryName,
            Integer hourlyRate
    ) {}

    public record ProfileResponse(
            Long id,
            String name,
            String gender,
            String region,
            String introduction,
            Boolean isVisible,
            String phone,
            String phoneMessage,
            List<SpecialtyResponse> specialties
    ) {}

    public record ProfileUpdateRequest(
            String name,
            String gender,
            String region,
            String introduction,
            Boolean isVisible,
            String phone,
            String phoneMessage,
            List<SpecialtyRequest> specialties
    ) {}
}
