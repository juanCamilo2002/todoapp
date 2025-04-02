package com.firstproject.todoapp.modules.tasks.repository;

import com.firstproject.todoapp.modules.tasks.entity.Task;
import com.firstproject.todoapp.modules.tasks.enums.TaskStatus;
import com.firstproject.todoapp.modules.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findAllByUserAndIsActiveTrue(User user, Pageable pageable);

    @Query("""
                SELECT t FROM Task t WHERE t.user = :user
                AND (:status IS NULL OR t.status = :status)
                AND (:isActive IS NULL OR t.isActive = :isActive)
                AND (:search IS NULL OR
                    LOWER(COALESCE(CAST(t.title AS string), '')) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
                    OR LOWER(COALESCE(CAST(t.description AS string), '')) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')))
                AND (COALESCE(:dueDateFrom, NULL) IS NULL OR t.dueDate >= :dueDateFrom)
                AND (COALESCE(:dueDateTo, NULL) IS NULL OR t.dueDate <= :dueDateTo)
            """)
    Page<Task> findTasksByFilters(
            @Param("user") User user,
            @Param("status") TaskStatus status,
            @Param("isActive") Boolean isActive,
            @Param("search") String search,
            @Param("dueDateFrom") LocalDateTime dueDateFrom,
            @Param("dueDateTo") LocalDateTime dueDateTo,
            Pageable pageable);


}
