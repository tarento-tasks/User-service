
package com.example.Userservice.Model;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "skills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) 
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "skill_id",columnDefinition = "UUID",  nullable = false)
    private UUID skillId;

    @Column(name = "skill_name", nullable = false, unique = true)
    private String skillName;

    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SkillMapping> skillMappings;
}
