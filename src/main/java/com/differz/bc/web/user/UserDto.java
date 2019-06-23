package com.differz.bc.web.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {
    @JsonProperty("user_id")
    private UUID userId;
    @JsonProperty("user_name")
    private String username;
}
