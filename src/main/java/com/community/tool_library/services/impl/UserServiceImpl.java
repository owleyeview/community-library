package com.community.tool_library.services.impl;

import com.community.tool_library.dtos.AdminViewUserDTO;
import com.community.tool_library.dtos.UserDTO;
import com.community.tool_library.dtos.UserRegistrationDTO;
import com.community.tool_library.models.User;
import com.community.tool_library.repositories.UserRepository;
import com.community.tool_library.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //  implement UserService methods
    @Override
    public UserDTO registerUser(UserRegistrationDTO registrationDTO) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(registrationDTO.username())) {
            throw new RuntimeException("Username already taken");
        }
        if (userRepository.existsByEmail(registrationDTO.email())) {
            throw new RuntimeException("An account with this email already exists");
        }

        User newUser = new User();
        newUser.setUsername(registrationDTO.username());
        newUser.setEmail(registrationDTO.email());
        newUser.setPassword(passwordEncoder.encode(registrationDTO.password()));
        newUser.setRole("USER");

        User savedUser = userRepository.save(newUser);
        return mapToDTO(savedUser);
    }

    @Override
    public UserDTO getUser(Long userId) {
        User user = getUserEntity(userId);
        return mapToDTO(user);
    }

    @Override
    public Long getUserId(String username) {
        User user = userRepository.findByUsername(username);
        return user.getId();
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        User user = getUserEntity(userDTO.id());
        user.setUsername(userDTO.username());
        user.setEmail(userDTO.email());
        User updatedUser = userRepository.save(user);
        return mapToDTO(updatedUser);
    }

    @Override
    public AdminViewUserDTO getUserForAdmin(Long userId) {
        User user = getUserEntity(userId);
        return mapToAdminUserDetailDTO(user);
    }

    @Override
    public AdminViewUserDTO adminUpdateUser(AdminViewUserDTO userDTO) {
        User user = getUserEntity(userDTO.id());
        user.setUsername(userDTO.username());
        user.setEmail(userDTO.email());
        user.setRole(userDTO.role());
        User updatedUser = userRepository.save(user);
        return mapToAdminUserDetailDTO(updatedUser);
    }

    @Override
    public void setAdminRole(Long userId) {
        // Role check is handled by @PreAuthorize annotation
        User user = getUserEntity(userId);
        user.setRole("ADMIN");
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        User currentUser = getAuthenticatedUser();
        if (currentUser.getId().equals(userId)) {
            throw new RuntimeException("Admin cannot delete their own account");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public User getUserEntity(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID " + userId));
    }

    // Helper methods
    private User mapToEntity(UserDTO userDTO) {
        return User.builder()
                .username(userDTO.username())
                .email(userDTO.email())
                .build();
    }

    private UserDTO mapToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }

    private AdminViewUserDTO mapToAdminUserDetailDTO(User user) {
        return new AdminViewUserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getLastLogin(),
                user.getCreatedAt()
        );
    }

    // this method may not be necessary as we're using Spring Security
    private User getAuthenticatedUser() {
        // get the current Authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // ensure the user is authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }

        // get the username from the Authentication object
        String username = authentication.getName();

        // find the user by username
        return userRepository.findByUsername(username);
    }
}
