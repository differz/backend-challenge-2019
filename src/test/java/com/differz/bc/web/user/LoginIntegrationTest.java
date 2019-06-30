package com.differz.bc.web.user;

import com.differz.bc.core.User;
import com.differz.bc.dao.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@Transactional
class LoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @Test
    void loginUserMustReturnCredentials() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"user_name\": \"KiwiBird\", \"password\": \"123456\"}")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.user_name").value("KiwiBird"))
                .andExpect(jsonPath("$.credentials").value("S2l3aUJpcmQ6MTIzNDU2"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void loginNewUserMustRegisterInRepository() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"user_name\": \"KiwiBird\", \"password\": \"123456\"}"));
        assertTrue(userRepository.findByUsername("KiwiBird").isPresent());
    }

    @Test
    void loginUserMustReturnExisting() throws Exception {
        User user = new User();
        user.setUsername("KiwiBird");
        user.setPassword("123456");
        user.setCredentials("S2l3aUJpcmQ6MTIzNDU2");
        userRepository.save(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"user_name\": \"KiwiBird\", \"password\": \"123456\"}"));
        assertTrue(userRepository.findByCredentials("S2l3aUJpcmQ6MTIzNDU2").isPresent());
    }

    @Test
    @WithMockUser
    void getUsersMustReturnEmptyArrayOnEmptyRepository() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$", Matchers.hasSize(0)))
                .andDo(MockMvcResultHandlers.print());
    }
}
