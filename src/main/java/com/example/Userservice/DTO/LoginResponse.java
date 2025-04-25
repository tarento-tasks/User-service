package com.example.Userservice.DTO;
import java.util.UUID;
import lombok.Data;
 
@Data
public class LoginResponse {
    private String token;
    private String role;
    private UUID userId;
  
}
