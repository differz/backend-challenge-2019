package com.differz.bc.web.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class MessageInputDto {
    @NotNull
    @Size(max = 1000)
    @JsonProperty("message")
    private String message;
    @JsonProperty("room_id")
    private UUID roomId;
    @JsonProperty("user_id")
    private UUID userId;
}
