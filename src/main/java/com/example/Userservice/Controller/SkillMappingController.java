package com.example.Userservice.Controller;

import com.example.Userservice.DTO.ApiResponse;
import com.example.Userservice.DTO.SkillMappingRequest;
import com.example.Userservice.Model.SkillMapping;
import com.example.Userservice.Service.SkillMappingService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/skill-mapping")
public class SkillMappingController {

    private final SkillMappingService skillMappingService;

    public SkillMappingController(SkillMappingService skillMappingService) {
        this.skillMappingService = skillMappingService;
    }

    // 🔹 Unified GET: Fetch mappings by userId or skillId (or all if none provided)
    @GetMapping
    @PreAuthorize("hasAnyRole('MENTOR', 'ADMIN', 'STUDENT')")
    public ResponseEntity<ApiResponse<List<SkillMappingRequest>>> getSkillMappings(
            @RequestParam Optional<UUID> userId,
            @RequestParam Optional<UUID> skillId) {
 
        List<SkillMappingRequest> skillMappings = skillMappingService.getSkillMappings(userId, skillId);
        ApiResponse<List<SkillMappingRequest>> response = new ApiResponse<>(200, "Skill mappings retrieved successfully", skillMappings);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users/{userId}/skills/{skillId}")
public ResponseEntity<SkillMappingRequest> addSkillToUser(
        @PathVariable UUID userId, 
        @PathVariable UUID skillId) {

    SkillMapping skillMapping = skillMappingService.addSkillToUser(userId, skillId);
    
    SkillMappingRequest response = new SkillMappingRequest(
            skillMapping.getUser().getUserId(), 
            skillMapping.getSkill().getSkillId()
    );

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}

}
