package com.differz.bc.web.message;

import com.differz.bc.core.Message;
import com.differz.bc.dao.MessageRepository;
import com.differz.bc.event.MessageSentEvent;
import com.differz.bc.exception.BadRequestException;
import com.differz.bc.web.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final RoomService roomService;
    private final MessageRepository messageRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public Message saveMessage(MessageInputDto messageInputDto) {
        UUID roomId = messageInputDto.getRoomId();
        UUID userId = messageInputDto.getUserId();
        if (!roomService.isUserInRoomOrBot(userId, roomId)) {
            throw new BadRequestException("no user " + userId + " in the room " + roomId);
        }
        Message message = new Message();
        message.setRoomId(roomId);
        message.setUserId(userId);
        message.setMessage(messageInputDto.getMessage());
        messageRepository.save(message);

        MessageSentEvent messageSentEvent = new MessageSentEvent(roomId, userId, message.getMessage());
        applicationEventPublisher.publishEvent(messageSentEvent);
        return message;
    }
}
