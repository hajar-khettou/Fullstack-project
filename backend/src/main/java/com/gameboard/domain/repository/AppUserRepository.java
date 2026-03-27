package com.gameboard.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gameboard.domain.entity.AppUser;
import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {

    Optional<AppUser> findByAuth0Sub(String auth0Sub);

    Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);
}