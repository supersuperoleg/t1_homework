package com.rakhimov.homework.dto;

public record TaskDto(
        Long id,
        String title,
        String description,
        Long userId) { }