package com.ugive.repositories;

import com.ugive.models.Role;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Cacheable("roles")
public interface RoleRepository extends JpaRepository<Role, Integer> {
    List<Role> findAllByIsDeletedFalse();
    Optional<Role> findByRoleName(String roleName);
}