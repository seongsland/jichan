package com.jichan.dto;

public class UserDto {

    public record ProfileResponse(
            Long id,
            String name,
            String gender,
            String region,
            String introduction,
            Boolean isVisible,
            String phone,
            String phoneMessage
    ) {}

    public record ProfileUpdateRequest(
            String name,
            String gender,
            String region,
            String introduction,
            Boolean isVisible,
            String phone,
            String phoneMessage
    ) {}
}
