package com.community.tool_library.services;

import com.community.tool_library.dtos.UserDTO;
import com.community.tool_library.models.User;

public interface UserService {

    UserDTO registerUser(UserDTO userDTO);

    UserDTO loginUser(String username, String rawPassword);

    UserDTO viewUserProfile(Long userId);

    void setAdminRole(Long userId, boolean isAdmin);

    void deleteUser(Long userId, boolean isAdmin);

    User getUserEntity(Long userId);
}
