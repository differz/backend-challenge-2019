package com.differz.bc.web.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class RoomInputDto {
    @NotNull
    @Size(max = 30)
    @JsonProperty("name")
    private String name;
    @JsonProperty("user_id")
    private UUID creatorId;
}
