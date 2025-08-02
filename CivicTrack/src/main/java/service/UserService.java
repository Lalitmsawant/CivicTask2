package service;

import dto.UserDTO;
import entity.User;
import enums.UserRole;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repository.UserRepository;



import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private NotificationService notificationService;


//    public UserDetailsServiceAutoConfiguration loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
//    }

    /**
     * Register a new user
     */
    public User registerUser(UserDTO userDTO, String password) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setFullName(userDTO.getFullName());
        user.setPhoneNumber(userDTO.getPhoneNumber());

        user.setRole(UserRole.CITIZEN);
        user.setEnabled(true);
        user.setEmailVerified(false);

        User savedUser = userRepository.save(user);

        // Send welcome email
        //notificationService.sendWelcomeEmail(savedUser);

        return savedUser;
    }

    /**
     * Find user by username
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Find user by email
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Update user profile
     */
    public User updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if email is being changed and if it's already taken
        if (!user.getEmail().equals(userDTO.getEmail()) &&
                userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        user.setEmail(userDTO.getEmail());
        user.setFullName(userDTO.getFullName());
        user.setPhoneNumber(userDTO.getPhoneNumber());

        return userRepository.save(user);
    }

    /**
     * Get all users (admin only)
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Update user role (admin only)
     */
    public User updateUserRole(Long userId, UserRole role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setRole(role);
        return userRepository.save(user);
    }

    /**
     * Enable/disable user (admin only)
     */
    public User toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setEnabled(!user.isEnabled());
        return userRepository.save(user);
    }

    /**
     * Convert User entity to DTO
     */
    public UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole());
        dto.setEnabled(user.isEnabled());
        dto.setEmailVerified(user.isEmailVerified());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

}
