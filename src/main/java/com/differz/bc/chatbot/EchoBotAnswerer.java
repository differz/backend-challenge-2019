package com.differz.bc.chatbot;

import static com.differz.bc.chatbot.ChatbotService.BOT_KEY_PHRASE;

public class EchoBotAnswerer implements ChatbotAnswerer {
    @Override
    public String getAnswer(String message) {
        return "User said " + message.replace(BOT_KEY_PHRASE, "");
    }
}
