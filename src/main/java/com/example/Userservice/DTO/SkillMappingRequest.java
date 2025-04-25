package com.example.Userservice.DTO;

import java.util.UUID;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SkillMappingRequest{
    
    private UUID userId;   // User ID
    private UUID skillId;  // Skill ID
}

