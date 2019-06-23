package com.differz.bc.web.user;

import com.differz.bc.core.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class LoginDto {
    @JsonProperty("user_id")
    private UUID userId;
    @JsonProperty("user_name")
    private String username;
    @JsonProperty("credentials")
    private String credentials;

    public static LoginDto of(User user) {
        LoginDto loginDto = new LoginDto();
        loginDto.setUserId(user.getId());
        loginDto.setUsername(user.getUsername());
        loginDto.setCredentials(user.getCredentials());
        return loginDto;
    }
}
