package com.differz.bc.web.user;

import com.differz.bc.core.User;
import com.differz.bc.dao.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@Transactional
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        User user1 = new User();
        user1.setId(UUID.fromString("b4173398-9171-44b4-a6d2-d7c7c5ef1697"));
        user1.setUsername("John Doe");
        user1.setPassword(passwordEncoder.encode("password"));
        user1.setCredentials(userService.getEncodedCredentials("John Doe", "password"));
        userRepository.save(user1);

        User user2 = new User();
        user2.setId(UUID.fromString("f2dc5a4e-0ebe-4138-a02b-283b37007cb9"));
        user2.setUsername("Jane Doe");
        user2.setPassword(passwordEncoder.encode("password"));
        user2.setCredentials(userService.getEncodedCredentials("Jane Doe", "password"));
        userRepository.save(user2);
    }

    @Test
    @WithMockUser
    void getUsersMustReturnArrayFixedSizeArray() throws Exception {
        getUsers()
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }

    @Test
    @WithMockUser
    void getUsersMustReturnUsername() throws Exception {
        getUsers()
                .andExpect(jsonPath("$.[0].user_name").value("John Doe"))
                .andExpect(jsonPath("$.[1].user_name").value("Jane Doe"));
    }

    @Test
    @WithMockUser
    void getUsersMustReturnId() throws Exception {
        getUsers()
                .andExpect(jsonPath("$.[0].user_id").value("b4173398-9171-44b4-a6d2-d7c7c5ef1697"))
                .andExpect(jsonPath("$.[1].user_id").value("f2dc5a4e-0ebe-4138-a02b-283b37007cb9"));
    }

    @Test
    @WithMockUser
    void getUserByIdReturnObject() throws Exception {
        getUri("/users/b4173398-9171-44b4-a6d2-d7c7c5ef1697")
                .andExpect(jsonPath("$.user_id").value("b4173398-9171-44b4-a6d2-d7c7c5ef1697"))
                .andExpect(jsonPath("$.user_name").value("John Doe"));
    }

    @Test
    @WithMockUser
    void getUserByIdMustReturnNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/00000000-0000-0000-0000-000000000000")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getUsersMustReturnUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void getUserByIdMustReturnUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/b4173398-9171-44b4-a6d2-d7c7c5ef1697")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    private ResultActions getUsers() throws Exception {
        return getUri("/users");
    }

    private ResultActions getUri(String uri) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andDo(MockMvcResultHandlers.print());
    }
}
