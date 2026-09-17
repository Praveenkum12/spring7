package com.jimmy.portal.auth.repository;

import com.jimmy.portal.auth.entity.Role;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Cacheable(value = "roles", key = "#name")
    Optional<Role> findByName(String name);

}
