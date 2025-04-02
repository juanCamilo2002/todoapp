package com.firstproject.todoapp.modules.tasks.service;

import com.firstproject.todoapp.modules.tasks.dto.*;
import com.firstproject.todoapp.modules.tasks.entity.Task;
import com.firstproject.todoapp.modules.tasks.enums.TaskStatus;
import com.firstproject.todoapp.modules.tasks.mapper.TaskMapper;
import com.firstproject.todoapp.modules.tasks.repository.TaskRepository;
import com.firstproject.todoapp.modules.users.entity.User;
import com.firstproject.todoapp.shared.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskResponseDTO createTask(TaskRequestDTO dto, User user) {
        Task task = taskMapper.toEntity(dto);
        task.setUser(user);
        Task saved = taskRepository.save(task);
        return taskMapper.toDTO(saved);
    }

    public Page<TaskResponseDTO> getUserTasks(
            User user,
            Pageable pageable,
            TaskStatus status,
            Boolean isActive,
            String search,
            LocalDateTime dueDateFrom,
            LocalDateTime dueDateTo
    ) {
        return taskRepository.findTasksByFilters(user, status, isActive, search, dueDateFrom, dueDateTo, pageable)
                .map(taskMapper::toDTO);
    }

    public TaskResponseDTO updateTask(Long taskId, TaskUpdateDTO dto, User user) {
        Task task = getTaskByIdAndUser(taskId, user);
        taskMapper.updateTaskFromDTO(dto, task);
        return taskMapper.toDTO(taskRepository.save(task));
    }

    public void deleteTask(Long taskId, User user) {
        Task task = getTaskByIdAndUser(taskId, user);
        task.setIsActive(false);
        taskRepository.save(task);
    }

    private Task getTaskByIdAndUser(Long id, User user) {
        return taskRepository.findById(id)
                .filter(t -> t.getUser().getId().equals(user.getId()) && Boolean.TRUE.equals(t.getIsActive()))
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
    }


}
