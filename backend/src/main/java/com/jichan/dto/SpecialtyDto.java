package com.jichan.dto;

public class SpecialtyDto {

    public record CategoryResponse(
            Long id,
            String name
    ) {}

    public record DetailResponse(
            Long id,
            String name,
            Long categoryId
    ) {}
}
