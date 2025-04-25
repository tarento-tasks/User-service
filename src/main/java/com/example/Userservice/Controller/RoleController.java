package com.example.Userservice.Controller;
import org.springframework.dao.DataIntegrityViolationException;
import com.example.Userservice.DTO.RoleDTO;
import com.example.Userservice.Model.Role;
import com.example.Userservice.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.example.Userservice.DTO.ApiResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
 
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/roles")
public class RoleController {
 
    @Autowired
    private RoleService roleService;
 
@GetMapping
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<ApiResponse<?>> getRoles(@RequestParam(required = false) UUID roleId,
                                               @RequestParam(required = false) String roleName) {
    if (roleId != null) {
        Optional<RoleDTO> role = roleService.getRoleById(roleId);
        if (role.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Role found", role.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Role not found", null));
        }
    }
 
    if (roleName != null) {
        Optional<RoleDTO> role = roleService.getRoleByName(roleName);
        if (role.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Role found", role.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Role not found", null));
        }
    }
 
    List<RoleDTO> roles = roleService.getAllRoles();
    return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Roles retrieved successfully", roles));
}
 
 
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RoleDTO>> createRole(@RequestBody RoleDTO roleDTO) {
        try {
            RoleDTO savedRole = roleService.saveRole(roleDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(HttpStatus.CREATED.value(), "Role created successfully", savedRole));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Role name already exists", null));
        }
    }
 
 
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable UUID id) {
        boolean deleted = roleService.deleteRole(id);
        if (deleted) {
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Role deleted successfully", null));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Role not found or already deleted", null));
    }
}