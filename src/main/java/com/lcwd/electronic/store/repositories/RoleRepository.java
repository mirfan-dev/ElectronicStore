package com.lcwd.electronic.store.repositories;

import com.lcwd.electronic.store.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,String> {

    Optional<Role> findFirstByRoleName(String roleName);
}
