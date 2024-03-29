package com.differz.bc.dao;

import com.differz.bc.core.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    Optional<User> findByCredentials(String credentials);

    Optional<User> findByUsernameAndBotTrue(String username);

    Optional<User> findByIdAndBotTrue(UUID uuid);

}
