package com.jichan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ContactDto {

    @Schema(description = "특기 정보")
    public record SpecialtyInfo(
            @Schema(description = "특기 이름", example = "Java 개발") String name,
            @Schema(description = "시간당 요금", example = "50000") Integer hourlyRate,
            @Schema(description = "특기 상세 ID", example = "1") Long specialtyDetailId) {
    }

    @Schema(description = "지인 목록 응답")
    public record ContactListResponse(
            @Schema(description = "전문가 ID", example = "1") Long expertId,
            @Schema(description = "전문가 이름", example = "홍길동") String expertName,
            @Schema(description = "성별", example = "MALE") String gender,
            @Schema(description = "지역", example = "서울") String region,
            @Schema(description = "특기 목록") List<SpecialtyInfo> specialties,
            @Schema(description = "자기소개", example = "안녕하세요.") String introduction,
            @Schema(description = "이메일 공개 여부", example = "true") Boolean hasEmailView,
            @Schema(description = "전화번호 공개 여부", example = "true") Boolean hasPhoneView,
            @Schema(description = "평점", example = "5") Integer rating,
            @Schema(description = "이메일", example = "expert@example.com") String email,
            @Schema(description = "전화번호", example = "010-1234-5678") String phone,
            @Schema(description = "연락 가능 시간 메모", example = "오후 6시 이후") String phoneMessage) {
    }

    @Schema(description = "지인 목록 슬라이스 응답")
    public record ContactSliceResponse(
            @Schema(description = "연락처 목록") List<ContactListResponse> content,
            @Schema(description = "다음 페이지 존재 여부", example = "true") boolean hasNext) {
    }

    @Schema(description = "평가 요청")
    public record RatingRequest(
            @Schema(description = "전문가 ID", example = "1")
            @NotNull(message = "전문가 ID는 필수입니다") Long expertId,

            @Schema(description = "점수 (1~5)", example = "5")
            @NotNull(message = "점수는 필수입니다") @Min(value = 1, message = "점수는 1 이상이어야 합니다") @Max(value = 5, message = "점수는 5 이하여야 합니다") Integer score) {
    }

    @Schema(description = "평가 응답")
    public record RatingResponse(
            @Schema(description = "평가 ID", example = "1") Long id,
            @Schema(description = "전문가 ID", example = "1") Long expertId,
            @Schema(description = "점수", example = "5") Integer score) {
    }

    @Schema(description = "평가 통계")
    public record RatingStatsDto(
            @Schema(description = "평균 점수", example = "4.5") Double average,
            @Schema(description = "평가 수", example = "10") Long count) {
    }

}
