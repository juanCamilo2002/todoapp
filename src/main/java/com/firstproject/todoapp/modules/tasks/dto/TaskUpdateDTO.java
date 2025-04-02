package com.firstproject.todoapp.modules.tasks.dto;

import com.firstproject.todoapp.modules.tasks.enums.TaskStatus;
import com.firstproject.todoapp.modules.tasks.validation.EnumValid;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskUpdateDTO {
    private String title;
    private String description;

    @EnumValid(enumClass = TaskStatus.class, message = "Invalid status. Allowed values: PENDING, IN_PROGRESS, COMPLETED")
    private String status;

    private LocalDateTime dueDate;
}
