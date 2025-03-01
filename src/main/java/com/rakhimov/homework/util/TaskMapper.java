package com.rakhimov.homework.util;

import com.rakhimov.homework.dto.TaskDto;
import com.rakhimov.homework.entity.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toEntity(TaskDto taskDto);

    TaskDto toDto(Task task);
}