package com.jichan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public class ProfileDto {

    @Schema(description = "프로필 검색 요청")
    public record ProfileRequest(
            @Schema(description = "카테고리 ID", example = "1") Long category,
            @Schema(description = "전문 분야 ID", example = "1") Long specialty,
            @Schema(description = "정렬 기준", example = "rating") String sortBy,
            @Schema(description = "지역", example = "서울") String region,
            @Schema(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam(defaultValue = "0") int page) {
    }

    @Schema(description = "연락처 조회 요청")
    public record ContactViewRequest(
            @Schema(description = "전문가 ID", example = "1")
            @NotNull(message = "전문가 ID는 필수입니다") Long expertId,

            @Schema(description = "연락처 타입 (EMAIL/PHONE)", example = "EMAIL")
            @NotNull(message = "연락처 타입은 필수입니다") String contactType) {
    }

    @Schema(description = "연락처 조회 응답")
    public record ContactViewResponse(
            @Schema(description = "연락처 정보", example = "expert@example.com") String contact,
            @Schema(description = "연락 가능 시간 메모", example = "오후 6시 이후") String phoneMessage) {
    }

    @Schema(description = "특기 정보")
    public record SpecialtyInfo(
            @Schema(description = "특기 이름", example = "Java 개발") String name,
            @Schema(description = "시간당 금액", example = "50000") Integer hourlyRate,
            @Schema(description = "특기 상세 ID", example = "1") Long specialtyDetailId) {
    }

    @Schema(description = "프로필 항목")
    public record ProfileItem(
            @Schema(description = "전문가 ID", example = "1") Long id,
            @Schema(description = "이름", example = "홍길동") String name,
            @Schema(description = "성별", example = "MALE") String gender,
            @Schema(description = "지역", example = "서울") String region,
            @Schema(description = "특기 목록") List<SpecialtyInfo> specialties,
            @Schema(description = "자기소개", example = "안녕하세요.") String introduction,
            @Schema(description = "평균 평점", example = "4") Integer averageRating,
            @Schema(description = "리뷰 수", example = "10") Integer reviewCount,
            @Schema(description = "이메일 입력 여부", example = "true") boolean isEmailInputted,
            @Schema(description = "전화번호 입력 여부", example = "true") boolean isPhoneInputted,
            @Schema(description = "이메일 조회 여부", example = "false") boolean isEmailViewed,
            @Schema(description = "전화번호 조회 여부", example = "false") boolean isPhoneViewed,
            @Schema(description = "이메일", example = "expert@example.com") String email,
            @Schema(description = "전화번호", example = "010-1234-5678") String phone,
            @Schema(description = "연락 가능 시간 메모", example = "오후 6시 이후") String phoneMessage) {
    }

    @Schema(description = "프로필 목록 응답")
    public record ProfileListResponse(
            @Schema(description = "프로필 목록") List<ProfileItem> content,
            @Schema(description = "다음 페이지 존재 여부", example = "true") Boolean hasNext) {
    }
}
