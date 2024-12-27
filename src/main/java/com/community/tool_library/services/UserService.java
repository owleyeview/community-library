package com.community.tool_library.services;

import com.community.tool_library.dtos.AdminViewUserDTO;
import com.community.tool_library.dtos.ItemDTO;
import com.community.tool_library.dtos.UserDTO;
import com.community.tool_library.dtos.UserRegistrationDTO;
import com.community.tool_library.models.User;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface UserService {

    UserDTO registerUser(UserRegistrationDTO registrationDTO);

    UserDTO getUser(Long userId);

    Long getUserId(String username);

    UserDTO updateUser(UserDTO userDTO);

    AdminViewUserDTO getUserForAdmin(Long userId);

    AdminViewUserDTO adminUpdateUser(AdminViewUserDTO userDTO);

    @PreAuthorize("hasRole('ADMIN')")
    void setAdminRole(Long userId);

    @PreAuthorize("hasRole('ADMIN')")
    void deleteUser(Long userId);

    @PreAuthorize("hasRole('ADMIN')")
    List<AdminViewUserDTO> getAllUsers();

    @PreAuthorize("hasRole('ADMIN')")
    List<AdminViewUserDTO> searchUsers(String query);

    User getUserEntity(Long userId);
}
