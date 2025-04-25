package com.example.Userservice.Repository;

import com.example.Userservice.Model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SkillRepository extends JpaRepository<Skill, UUID> {
    Optional<Skill> findBySkillName(String skillName);
}
