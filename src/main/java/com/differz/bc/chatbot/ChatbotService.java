package com.differz.bc.chatbot;

import com.differz.bc.event.MessageSentEvent;
import com.differz.bc.web.message.MessageInputDto;
import com.differz.bc.web.message.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatbotService {

    static final String BOT_KEY_PHRASE = "@bot ";

    private final MessageService messageService;
    private final ChatbotAnswerer chatbotAnswerer;
    private final Chatbot chatbot;

    @Async
    @EventListener
    public void messageSentEvent(MessageSentEvent event) {
        UUID userId = event.getUserId();
        UUID roomId = event.getRoomId();
        String message = event.getMessage();

        if (!isOwnMessage(userId) && isMessageForBot(message)) {
            String answer = chatbotAnswerer.getAnswer(message);
            MessageInputDto messageFromBot = new MessageInputDto();
            messageFromBot.setMessage(answer);
            messageFromBot.setRoomId(roomId);
            messageFromBot.setUserId(getBotId());
            messageService.saveMessage(messageFromBot);
        }
    }

    private UUID getBotId() {
        return chatbot.getUser().getId();
    }

    private boolean isOwnMessage(UUID userId) {
        UUID botId = getBotId();
        return userId.equals(botId);
    }

    private boolean isMessageForBot(String message) {
        return message.startsWith(BOT_KEY_PHRASE);
    }
}
