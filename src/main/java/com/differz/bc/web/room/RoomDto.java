package com.differz.bc.web.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class RoomDto {
    @JsonProperty("room_id")
    private UUID roomId;
    @JsonProperty("room_name")
    private String roomName;
    @JsonProperty("creator_id")
    private UUID creatorId;
    @JsonProperty("created_at")
    private Instant createdAt;
}
