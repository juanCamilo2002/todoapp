package com.firstproject.todoapp.modules.tasks.controller;

import com.firstproject.todoapp.modules.tasks.dto.*;
import com.firstproject.todoapp.modules.tasks.enums.TaskStatus;
import com.firstproject.todoapp.modules.tasks.service.TaskService;
import com.firstproject.todoapp.modules.users.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Endpoints for managing user tasks.")
public class TaskController {
    private final TaskService taskService;

    @Operation(
            summary = "Endpoint for create user tasks",
            description = "This returns the task data"
    )
    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(
            @Valid @RequestBody TaskRequestDTO dto,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(dto, user));
    }

    @Operation(
            summary = "Endpoint to get all tasks by applying filters",
            description = "This returns the task data by applying pagination."
    )
    @GetMapping
    public ResponseEntity<Page<TaskResponseDTO>> getTasks(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) LocalDateTime dueDateFrom,
            @RequestParam(required = false) LocalDateTime dueDateTo) {
        Pageable pageable =  PageRequest.of(page, size);
        return ResponseEntity.ok(taskService.getUserTasks(user, pageable, status, isActive, search, dueDateFrom, dueDateTo));
    }

    @Operation(
            summary = "Endpoint for updating task data",
            description = "This returns the data of an updated task"
    )
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateDTO dto,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(taskService.updateTask(id, dto, user));
    }

    @Operation(
            summary = "Endpoint to delete a task",
            description = "This returns a task deletion confirmation response."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        taskService.deleteTask(id, user);
        return ResponseEntity.noContent().build();
    }
}
