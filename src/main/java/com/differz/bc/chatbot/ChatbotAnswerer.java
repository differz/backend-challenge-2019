package com.differz.bc.chatbot;

import java.util.UUID;

public interface ChatbotAnswerer {

    String getAnswer(String message);

    String getAnswer(String message, UUID userId);

}
