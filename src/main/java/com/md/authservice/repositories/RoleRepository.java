package com.md.authservice.repositories;

import com.md.authservice.entities.Role;
import com.md.authservice.entities.RoleEnum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends CrudRepository<Role, UUID> {
    Optional<Role> findByName(RoleEnum name);
}
