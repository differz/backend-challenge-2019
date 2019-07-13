package com.differz.bc.chatbot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

class OpenNlpBotAnswererTest {

    private OpenNlpBotAnswerer bot;

    @BeforeEach
    void setUp() {
        bot = new OpenNlpBotAnswerer(null, null, null, null);
    }

    @Test
    void detectDate() throws IOException {
        String msg = "on date 22.12.1984 or july 13 2019";
        String[] tokens = bot.tokenize(msg);
        String[] dates = bot.detectDate(tokens);
        System.out.println(Arrays.toString(dates));

    }

    @Test
    void detectTime() throws IOException {
        String msg = "at 08:30 pm or 13:30 or half past ten";
        String[] tokens = bot.tokenize(msg);
        String[] times = bot.detectTime(tokens);
        System.out.println(Arrays.toString(times));

    }
}