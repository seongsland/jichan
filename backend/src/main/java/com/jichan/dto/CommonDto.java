package com.jichan.dto;

public class CommonDto {

    public record ApiResponse<T>(String message, T data) {
    }
}
