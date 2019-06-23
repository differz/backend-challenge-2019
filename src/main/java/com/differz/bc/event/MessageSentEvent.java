package com.differz.bc.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class MessageSentEvent {
    private UUID roomId;
    private UUID userId;
    private String message;
}
