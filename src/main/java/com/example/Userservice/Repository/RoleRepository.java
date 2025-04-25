package com.example.Userservice.Repository;
 
import com.example.Userservice.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
 
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByRoleName(String roleName);

    @Query("SELECT r FROM Role r WHERE r.deletedAt IS NULL")
    List<Role> findAllActiveRoles();
 
    @Query("SELECT r FROM Role r WHERE r.roleId = :roleId AND r.deletedAt IS NULL")
    Optional<Role> findActiveRoleById(@Param("roleId") UUID roleId);
 
    @Query("SELECT r FROM Role r WHERE r.roleName = :roleName AND r.deletedAt IS NULL")
    Optional<Role> findActiveRoleByName(@Param("roleName") String roleName);
}