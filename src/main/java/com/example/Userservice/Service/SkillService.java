package com.example.Userservice.Service;

import com.example.Userservice.DTO.SkillDTO;
import com.example.Userservice.Exception.AlreadyExistsException;
import com.example.Userservice.Exception.BadRequestException;
import com.example.Userservice.Exception.ResourceNotFoundException;
import com.example.Userservice.Model.Skill;
import com.example.Userservice.Repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    
    public List<SkillDTO> getAllSkills() {
        return skillRepository.findAll()
                .stream()
                .map(skill -> new SkillDTO(skill.getSkillId(), skill.getSkillName()))
                .collect(Collectors.toList());
    }

    
    public SkillDTO getSkillById(UUID id) {
        return skillRepository.findById(id)
                .map(skill -> new SkillDTO(skill.getSkillId(), skill.getSkillName()))
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));
    }

    
    @Transactional
public SkillDTO createOrUpdateSkill(UUID id, SkillDTO skillDTO) {
    if (skillDTO.getSkillName() == null || skillDTO.getSkillName().trim().isEmpty()) {
        throw new BadRequestException("Skill name cannot be empty");
    }

    Skill skill;

    
    if (id != null) {
        skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));
    } else {
        skill = new Skill();
    }

    // Check for duplicate skill name (only if creating OR updating to a new name)
    Optional<Skill> existingSkill = skillRepository.findBySkillName(skillDTO.getSkillName());
    if (existingSkill.isPresent() && !existingSkill.get().getSkillId().equals(id)) {
        throw new AlreadyExistsException("Skill with this name already exists");
    }

    skill.setSkillName(skillDTO.getSkillName());
    Skill savedSkill = skillRepository.save(skill);
    return new SkillDTO(savedSkill.getSkillId(), savedSkill.getSkillName());
}


    
    @Transactional
    public void deleteSkill(UUID id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));
        skillRepository.delete(skill);
    }
}
