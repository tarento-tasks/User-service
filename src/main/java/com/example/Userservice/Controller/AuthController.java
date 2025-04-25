package com.example.Userservice.Controller;
import com.example.Userservice.Model.User;
import com.example.Userservice.Repository.UserRepository;
import com.example.Userservice.Service.AuthService;
import com.example.Userservice.config.JwtUtil;
import com.example.Userservice.DTO.ApiResponse;
import com.example.Userservice.DTO.LoginRequest;
import com.example.Userservice.DTO.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173") // Allow only requests from this origin

@RequestMapping("/api/auth") 
public class AuthController {

@Autowired
private AuthenticationManager authenticationManager;

@Autowired
private AuthService authService;

@Autowired
private JwtUtil jwtUtil;

@Autowired
private UserRepository userRepository;

public AuthController(AuthService authService) {
this.authService = authService;
}

@PostMapping("/login") 
public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

return ResponseEntity.ok(authService.login(loginRequest));
}

@PostMapping("/logout")
public ResponseEntity<ApiResponse<String>> logout(@RequestHeader("Authorization") String token) {
    if (token == null || !token.startsWith("Bearer ")) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(400, "Invalid token format", null));
    }

    String jwt = token.substring(7);
    jwtUtil.invalidateToken(jwt); 
    return ResponseEntity.ok(new ApiResponse<>(200, "Logout successful", null));
}

} 