package com.internship.tool.repository;

import com.internship.tool.entity.Role;
import com.internship.tool.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for User entities.
 * Day 9: Added findByRole for admin user-management endpoints.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    // Day 9 — RBAC / Admin
    List<User> findByRole(Role role);
}
