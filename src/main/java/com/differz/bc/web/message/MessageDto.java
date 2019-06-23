package com.differz.bc.web.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class MessageDto {
    @JsonProperty("message")
    private String message;
    @JsonProperty("room_id")
    private UUID roomId;
    @JsonProperty("user_id")
    private UUID userId;
}
