package com.differz.bc.web.message;

import com.differz.bc.core.Room;
import com.differz.bc.core.User;
import com.differz.bc.event.MessageSentEvent;
import com.differz.bc.web.room.RoomService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageListener {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ObjectMapper objectMapper;
    private final RoomService roomService;

    @Async
    @EventListener
    public void messageSentEvent(MessageSentEvent event) throws JsonProcessingException {
        UUID userId = event.getUserId();
        UUID roomId = event.getRoomId();
        String message = event.getMessage();

        MessageDto messageDto = new MessageDto(message, roomId, userId);
        String jsonMessage = objectMapper.writeValueAsString(messageDto);

        Room room = roomService.getRoomByIdOrThrow(roomId);
        for (User user : room.getUsers()) {
            String username = user.getUsername();
            messagingTemplate.convertAndSendToUser(username, "/queue/reply", jsonMessage);
            log.debug("message sent to user " + username);
        }
    }
}
