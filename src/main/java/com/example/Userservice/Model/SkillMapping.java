package com.example.Userservice.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "skill_mapping")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillMapping {

    @EmbeddedId
    private SkillMappingId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("skillId")
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;
}
