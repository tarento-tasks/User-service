package com.example.Userservice.Controller;

import com.example.Userservice.DTO.ApiResponse;
import com.example.Userservice.DTO.UserDTO;
import com.example.Userservice.Model.User;
import com.example.Userservice.Service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    
    @GetMapping
@PreAuthorize("hasAnyRole('MENTOR', 'ADMIN', 'STUDENT')")
public ResponseEntity<ApiResponse<?>> getUsers(
        @RequestParam(required = false) UUID userId,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String role) {


            System.out.println("userId: " + userId);
    System.out.println("email: " + email);
    System.out.println("role: " + role);
    
    if (userId != null) {
        UserDTO user = userService.getUserById(userId).orElse(null);
        if (user != null) {
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "User fetched successfully", user));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "User not found", null));
        }
    }

    if (email != null) {
        UserDTO user = userService.getUserByEmail(email).orElse(null);
        if (user != null) {
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "User fetched successfully", user));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "User not found", null));
        }
    }

    if (role != null) {
        String cleanRole = role.toUpperCase().replace("ROLE_", "");
        if (!List.of("ADMIN", "MENTOR", "STUDENT").contains(cleanRole)) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                          "Invalid role. Must be ADMIN, MENTOR, or STUDENT", null));
        }

        List<UserDTO> users = userService.getUsersByRole(cleanRole);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Users fetched successfully", users));
    }

    List<UserDTO> users = userService.getAllUsers();
    return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Users fetched successfully", users));
}


    
    @PostMapping
    @PreAuthorize("hasAnyRole('MENTOR', 'ADMIN', 'STUDENT')")
    public ResponseEntity<ApiResponse<UserDTO>> createOrUpdateUser(
            @RequestParam(required = false) UUID userId,
            @RequestParam String email,
            @RequestParam(required = false) String password,
            @RequestParam String name,
            @RequestParam (required = false) String dob,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(required = false) String previousWork,
            @RequestParam(required = false) String qualifications,
            @RequestParam UUID roleId) {
 
        try {
            UserDTO userDTO = userService.convertToDTO(
                    userService.createOrUpdateUser(userId, email, password, name, dob, image, previousWork, qualifications, roleId)
            );
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "User saved successfully", userDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> softDeleteUser(@PathVariable UUID id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "User deleted successfully", null));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "User not found or already deleted", null));
    }
 
}
