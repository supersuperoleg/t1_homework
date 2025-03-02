package com.rakhimov.homework.util;

import com.rakhimov.homework.dto.TaskDto;
import com.rakhimov.homework.entity.Task;
import com.rakhimov.homework.enums.TaskStatus;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toEntity(TaskDto dto) {
        return Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(TaskStatus.fromString(dto.getStatus()))
                .userId(dto.getUserId())
                .build();
    }

    public TaskDto toDto(Task entity) {
        return TaskDto.builder()
                .title(entity.getTitle())
                .description(entity.getDescription())
                .status(entity.getStatus().toString())
                .userId(entity.getUserId())
                .build();
    }
}