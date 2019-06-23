package com.differz.bc.web.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class UserIdDto {
    @JsonProperty("user_id")
    private UUID userId;
}
