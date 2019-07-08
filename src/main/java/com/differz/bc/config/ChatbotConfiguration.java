package com.differz.bc.config;

import com.differz.bc.chatbot.Chatbot;
import com.differz.bc.chatbot.ChatbotAnswerer;
import com.differz.bc.chatbot.EchoBotAnswerer;
import com.differz.bc.core.User;
import com.differz.bc.web.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ChatbotConfiguration {

    private static final String CHATBOT = "chatbot";

    private final UserService userService;

    @Bean
    public Chatbot chatbot() {
        User user = userService.findBotUser(CHATBOT)
                .orElseGet(() -> userService.registerBotUser(CHATBOT));
        return new Chatbot(user);
    }

    @Bean
    public ChatbotAnswerer chatbotAnswerer() {
        return new EchoBotAnswerer();
    }
}
