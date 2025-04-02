package com.firstproject.todoapp.modules.tasks.mapper;



import com.firstproject.todoapp.modules.tasks.dto.*;
import com.firstproject.todoapp.modules.tasks.entity.Task;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toEntity(TaskRequestDTO dto);

    TaskResponseDTO toDTO(Task entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTaskFromDTO(TaskUpdateDTO dto, @MappingTarget Task entity);
}
