package controller;


import dto.UserDTO;
import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    //@PreAuthorize("hasRole('CITIZEN')")
    public ResponseEntity<UserDTO> getProfile(User user) {
        UserDTO userDTO = userService.convertToDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/profile")

    public ResponseEntity<?> updateProfile(
            @Validated @RequestBody UserDTO userDTO,
            User user) {
        try {
            User updatedUser = userService.updateUser(user.getId(), userDTO);
            UserDTO responseDTO = userService.convertToDTO(updatedUser);
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
