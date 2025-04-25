package com.example.Userservice.Model;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;
 
@Entity
@Table(name = "roles")
public class Role {
 
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID roleId;
 
    @Column(nullable = false, unique = true)
    private String roleName;
    
    private LocalDateTime deletedAt;
   
    public Role() {}
 
    
    public Role(String roleName) {
        this.roleName = roleName;
    }
 
    
    public Role(UUID roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }
 
    
    public UUID getRoleId() {
        return roleId;
    }
 
    
    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }
 
    
    public String getRoleName() {
        return roleName;
    }
 
    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }
 
    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}