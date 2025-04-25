package com.example.Userservice.Repository;
import com.example.Userservice.Model.SkillMapping;
import com.example.Userservice.Model.SkillMappingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface SkillMappingRepository extends JpaRepository<SkillMapping, SkillMappingId> {
    List<SkillMapping> findBySkill_SkillId(UUID skillId);

    
    List<SkillMapping> findByUser_UserId(UUID userId);
}


