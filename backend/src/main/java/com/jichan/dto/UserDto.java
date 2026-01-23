package com.jichan.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
            @Size(max = 200, message = "소개글은 200자 이내여야 합니다.")
            @Pattern(regexp = "^([^\\n]*\\n){0,4}[^\\n]*$", message = "소개글은 최대 5줄까지 입력 가능합니다.")
            String introduction,
            Boolean isVisible,
            String phone,
            @Size(max = 100, message = "연락 가능 시간 메모는 100자 이내여야 합니다.")
            @Pattern(regexp = "^([^\\n]*\\n){0,2}[^\\n]*$", message = "연락 가능 시간 메모는 최대 3줄까지 입력 가능합니다.")
            String phoneMessage,
            List<SpecialtyRequest> specialties
    ) {}
}
