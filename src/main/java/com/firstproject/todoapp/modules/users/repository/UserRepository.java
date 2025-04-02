package com.firstproject.todoapp.modules.users.repository;

import com.firstproject.todoapp.modules.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);


    @Query(""" 
    SELECT u FROM User u
    WHERE (:username IS NULL OR u.username LIKE %:username%)
    AND (:role IS NULL OR u.role = :role)
    AND (:isActive IS NULL OR u.isActive = :isActive)
    """)
    Page<User> findAllByFilters(@Param("username") String username,
                                @Param("role") String role,
                                @Param("isActive") Boolean isActive,
                                Pageable pageable);
}
