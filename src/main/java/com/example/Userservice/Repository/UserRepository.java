package com.example.Userservice.Repository;


import com.example.Userservice.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    
    List<User> findByDeletedAtIsNull();
    Optional<User> findByEmailAndDeletedAtIsNull(String email);
    Optional<User> findByUserIdAndDeletedAtIsNull(UUID userId);
    @Query("SELECT u FROM User u JOIN u.role r WHERE r.roleName = :roleName AND u.deletedAt IS NULL")
    List<User> findByRoleName(@Param("roleName") String roleName);
    List<User> findByRole_RoleName(String roleName);

}