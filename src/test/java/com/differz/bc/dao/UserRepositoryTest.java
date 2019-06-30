package com.differz.bc.dao;

import com.differz.bc.core.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Base64;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest()
class UserRepositoryTest {

    private static final UUID ID = UUID.fromString("b4173398-9171-44b4-a6d2-d7c7c5ef1697");

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(ID);
        user.setUsername("KiwiBird");
        user.setPassword("123456");
        user.setCredentials(Base64.getEncoder().encodeToString("KiwiBird:123456".getBytes()));
        userRepository.save(user);
    }

    @Test
    void findById() {
        assertTrue(userRepository.findById(ID).isPresent());
    }

    @Test
    void findByUsername() {
        assertTrue(userRepository.findByUsername("KiwiBird").isPresent());
    }

    @Test
    void findByCredentials() {
        assertTrue(userRepository.findByCredentials("S2l3aUJpcmQ6MTIzNDU2").isPresent());
    }
}
