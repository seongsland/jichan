package com.jichan.dto;

import com.jichan.util.validator.ValidName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public class UserDto {

    @Schema(description = "특기 요청")
    public record SpecialtyRequest(
            @Schema(description = "특기 상세 ID", example = "1") Long specialtyDetailId,
            @Schema(description = "시간당 요금", example = "50000") Integer hourlyRate) {
    }

    @Schema(description = "특기 응답")
    public record SpecialtyResponse(
            @Schema(description = "특기 상세 ID", example = "1") Long specialtyDetailId,
            @Schema(description = "특기 상세 이름", example = "Java 개발") String specialtyDetailName,
            @Schema(description = "특기 카테고리 이름", example = "IT/프로그래밍") String specialtyCategoryName,
            @Schema(description = "시간당 요금", example = "50000") Integer hourlyRate) {
    }

    @Schema(description = "프로필 응답")
    public record ProfileResponse(
            @Schema(description = "사용자 ID", example = "1") Long id,
            @Schema(description = "사용자 이름", example = "홍길동") String name,
            @Schema(description = "성별", example = "MALE") String gender,
            @Schema(description = "지역", example = "서울") String region,
            @Schema(description = "자기소개", example = "안녕하세요. 5년차 개발자입니다.") String introduction,
            @Schema(description = "프로필 공개 여부", example = "true") Boolean isVisible,
            @Schema(description = "연락처", example = "010-1234-5678") String phone,
            @Schema(description = "연락 가능 시간 메모", example = "평일 오후 6시 이후 연락 가능합니다.") String phoneMessage,
            @Schema(description = "특기 목록") List<SpecialtyResponse> specialties) {
    }

    @Schema(description = "프로필 수정 요청")
    public record ProfileUpdateRequest(
            @Schema(description = "사용자 이름", example = "홍길동") @ValidName String name,
            @Schema(description = "성별", example = "MALE") String gender,
            @Schema(description = "지역", example = "서울") String region,
            @Schema(description = "자기소개 (최대 200자, 5줄)", example = "안녕하세요. 5년차 개발자입니다.")
            @Size(max = 200, message = "소개글은 200자 이내여야 합니다.") @Pattern(regexp = "^([^\\n]*\\n){0,4}[^\\n]*$", message = "소개글은 최대 5줄까지 입력 가능합니다.") String introduction,
            @Schema(description = "프로필 공개 여부", example = "true") Boolean isVisible,
            @Schema(description = "연락처", example = "010-1234-5678") String phone,
            @Schema(description = "연락 가능 시간 메모 (최대 100자, 3줄)", example = "평일 오후 6시 이후 연락 가능합니다.")
            @Size(max = 100, message = "연락 가능 시간 메모는 100자 이내여야 합니다.") @Pattern(regexp = "^([^\\n]*\\n){0,2}[^\\n]*$", message = "연락 가능 시간 메모는 최대 3줄까지 입력 가능합니다.") String phoneMessage,
            @Schema(description = "특기 목록") List<SpecialtyRequest> specialties) {
    }
}
