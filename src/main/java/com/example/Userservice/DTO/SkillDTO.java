package com.example.Userservice.DTO;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillDTO {
    private UUID skillId;
    private String skillName;
}
