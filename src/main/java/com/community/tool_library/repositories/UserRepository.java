package com.community.tool_library.repositories;

import com.community.tool_library.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    User findByUsernameOrEmail(String username, String email);
    List<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrRoleContainingIgnoreCase(String username, String email, String role);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
