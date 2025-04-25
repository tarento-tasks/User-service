package com.example.Userservice.Service;

import com.example.Userservice.DTO.SkillMappingRequest;
import com.example.Userservice.Exception.ResourceNotFoundException;
import com.example.Userservice.Exception.AlreadyExistsException;
import com.example.Userservice.Model.Skill;
import com.example.Userservice.Model.SkillMapping;
import com.example.Userservice.Model.SkillMappingId;
import com.example.Userservice.Model.User;
import com.example.Userservice.Repository.SkillMappingRepository;
import com.example.Userservice.Repository.SkillRepository;
import com.example.Userservice.Repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillMappingService {

    private final SkillMappingRepository skillMappingRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    private static final String USER_NOT_FOUND_MSG = "User not found or is deleted";
    private static final String SKILL_NOT_FOUND_MSG = "Skill not found";
    private static final String SKILL_MAPPING_EXISTS_MSG = "Skill mapping already exists";

    @Transactional
    public SkillMapping addSkillToUser(UUID userId, UUID skillId) {
        // Ensure user exists and is not soft deleted
        User user = userRepository.findById(userId)
                .filter(u -> u.getDeletedAt() == null)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG));

        // Ensure skill exists
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException(SKILL_NOT_FOUND_MSG));

        SkillMappingId id = new SkillMappingId(userId, skillId);

        // Check if mapping already exists
        if (skillMappingRepository.existsById(id)) {
            throw new AlreadyExistsException(SKILL_MAPPING_EXISTS_MSG);
        }

        // Create and save mapping
        SkillMapping skillMapping = SkillMapping.builder()
                .id(id)
                .user(user)
                .skill(skill)
                .build();

        return skillMappingRepository.save(skillMapping);
    }

    
    @Transactional
    public List<SkillMappingRequest> getSkillMappings(Optional<UUID> userId, Optional<UUID> skillId) {
        if (userId.isPresent()) {
            return skillMappingRepository.findByUser_UserId(userId.get()).stream()
                    .map(mapping -> new SkillMappingRequest(mapping.getUser().getUserId(), mapping.getSkill().getSkillId()))
                    .collect(Collectors.toList());
        } else if (skillId.isPresent()) {
            return skillMappingRepository.findBySkill_SkillId(skillId.get()).stream()
                    .map(mapping -> new SkillMappingRequest(mapping.getUser().getUserId(), mapping.getSkill().getSkillId()))
                    .collect(Collectors.toList());
        }
        return skillMappingRepository.findAll().stream()
                .map(mapping -> new SkillMappingRequest(mapping.getUser().getUserId(), mapping.getSkill().getSkillId()))
                .collect(Collectors.toList());
    }
 

    
    private SkillMappingRequest convertToDTO(SkillMapping mapping) {
        return new SkillMappingRequest(mapping.getUser().getUserId(), mapping.getSkill().getSkillId());
    }
}
