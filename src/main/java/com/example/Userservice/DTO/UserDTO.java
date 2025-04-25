package com.example.Userservice.DTO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private UUID userId;
    private String email;
    private String name;
    private String dob;
    private String previousWork;
    private String qualifications;
    private UUID roleId; 
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
    private String imageBase64; 

    

}
