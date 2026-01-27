package com.jichan.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class SpecialtyDto {

    @Schema(description = "카테고리 응답")
    public record CategoryResponse(
            @Schema(description = "카테고리 ID", example = "1") Long id,
            @Schema(description = "카테고리 이름", example = "IT/프로그래밍") String name) {
    }

    @Schema(description = "세부 항목 응답")
    public record DetailResponse(
            @Schema(description = "세부 항목 ID", example = "1") Long id,
            @Schema(description = "세부 항목 이름", example = "Java") String name,
            @Schema(description = "카테고리 ID", example = "1") Long categoryId) {
    }
}
