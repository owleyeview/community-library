package com.community.tool_library.services;

import com.community.tool_library.dtos.UserDTO;
import com.community.tool_library.dtos.UserRegistrationDTO;
import com.community.tool_library.models.User;
import org.springframework.security.access.prepost.PreAuthorize;

public interface UserService {

    UserDTO registerUser(UserRegistrationDTO registrationDTO);

    UserDTO viewUserProfile(Long userId);

    //  replace with Spring Security role check
    boolean isAdmin(Long userId);

    @PreAuthorize("hasRole('ADMIN')")
    void setAdminRole(Long userId);

    @PreAuthorize("hasRole('ADMIN') or #userId == principal.id")
    void deleteUser(Long userId);

    User getUserEntity(Long userId);
}
