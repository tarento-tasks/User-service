package com.example.Userservice.Service;
 
import com.example.Userservice.DTO.RoleDTO;
import com.example.Userservice.Model.Role;
import com.example.Userservice.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
 
@Service
public class RoleService {
 
    @Autowired
    private RoleRepository roleRepository;
 
    // ✅ Get all active roles
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAllActiveRoles().stream()
                .map(role -> new RoleDTO(role.getRoleId(), role.getRoleName()))
                .collect(Collectors.toList());
    }
 
    // ✅ Get role by ID (Only active)
    public Optional<RoleDTO> getRoleById(UUID roleId) {
        return roleRepository.findActiveRoleById(roleId)
                .map(role -> new RoleDTO(role.getRoleId(), role.getRoleName()));
    }
 
    // ✅ Get role by Name (Only active)
    public Optional<RoleDTO> getRoleByName(String roleName) {
        return roleRepository.findActiveRoleByName(roleName)
                .map(role -> new RoleDTO(role.getRoleId(), role.getRoleName()));
    }
 
    // ✅ Create a new role
    public RoleDTO saveRole(RoleDTO roleDTO) {
        Role role = new Role();
        role.setRoleName(roleDTO.getRoleName());
        Role savedRole = roleRepository.save(role);
        return new RoleDTO(savedRole.getRoleId(), savedRole.getRoleName());
    }
 
    // ✅ Soft delete a role
    public boolean deleteRole(UUID roleId) {
        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (roleOpt.isPresent()) {
            Role role = roleOpt.get();
            if (role.getDeletedAt() == null) {
                role.setDeletedAt(LocalDateTime.now()); // Soft delete
                roleRepository.save(role);
                return true;
            }
        }
        return false;
    }
}