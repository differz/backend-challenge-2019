package com.differz.bc.web.room;

import com.differz.bc.core.Room;
import com.differz.bc.core.User;
import com.differz.bc.dao.RoomRepository;
import com.differz.bc.dao.UserRepository;
import com.differz.bc.web.user.UserService;
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
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@Transactional
class RoomIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RoomRepository roomRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(UUID.fromString("b4173398-9171-44b4-a6d2-d7c7c5ef1697"));
        user1.setUsername("John Doe");
        user1.setPassword(passwordEncoder.encode("password"));
        user1.setCredentials(userService.getEncodedCredentials("John Doe", "password"));
        userRepository.save(user1);

        user2 = new User();
        user2.setId(UUID.fromString("f2dc5a4e-0ebe-4138-a02b-283b37007cb9"));
        user2.setUsername("Jane Doe");
        user2.setPassword(passwordEncoder.encode("password"));
        user2.setCredentials(userService.getEncodedCredentials("Jane Doe", "password"));
        userRepository.save(user2);
    }

    @Test
    @WithMockUser
    void getRoomsMustReturnEmptyArrayOnEmptyRepository() throws Exception {
        getRooms()
                .andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    @WithMockUser
    void getRoomsMustReturnRoomsWithUsers() throws Exception {
        Room room = new Room();
        room.setName("frontend");
        room.setCreatedAt(Instant.parse("2019-04-28T19:05:56Z"));
        room.setCreatorId(UUID.fromString("b4173398-9171-44b4-a6d2-d7c7c5ef1697"));
        room.setUsers(Set.of(user1, user2));
        roomRepository.save(room);

        getRooms()
                .andExpect(jsonPath("$", Matchers.hasSize(1)));
    }

    private ResultActions getRooms() throws Exception {
        return getUri("/rooms");
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