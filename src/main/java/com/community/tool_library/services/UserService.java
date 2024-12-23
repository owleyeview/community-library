package com.community.tool_library.services;

import com.community.tool_library.dtos.AdminUserDetailDTO;
import com.community.tool_library.dtos.UserDTO;
import com.community.tool_library.dtos.UserRegistrationDTO;
import com.community.tool_library.models.User;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface UserService {

    UserDTO registerUser(UserRegistrationDTO registrationDTO);

    UserDTO getUser(Long userId);

    UserDTO updateUser(UserDTO userDTO);

    AdminUserDetailDTO getUserForAdmin(Long userId);

    AdminUserDetailDTO adminUpdateUser(AdminUserDetailDTO userDTO);

    @PreAuthorize("hasRole('ADMIN')")
    void setAdminRole(Long userId);

    @PreAuthorize("hasRole('ADMIN') or #userId == principal.id")
    void deleteUser(Long userId);

    @PreAuthorize("hasRole('ADMIN')")
    List<UserDTO> getAllUsers();

    User getUserEntity(Long userId);
}
