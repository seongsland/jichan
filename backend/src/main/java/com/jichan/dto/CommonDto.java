package com.jichan.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class CommonDto {

    @Schema(description = "API 공통 응답")
    public record ApiResponse<T>(
            @Schema(description = "응답 메시지", example = "성공하였습니다.") String message,
            @Schema(description = "응답 데이터") T data) {
    }
}
