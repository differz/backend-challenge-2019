package com.differz.bc.config;

import com.differz.bc.chatbot.Chatbot;
import com.differz.bc.chatbot.ChatbotAnswerer;
import com.differz.bc.chatbot.OpenNlpBotAnswerer;
import com.differz.bc.core.User;
import com.differz.bc.dao.ItemRepository;
import com.differz.bc.dao.OrderRepository;
import com.differz.bc.web.user.UserService;
import lombok.RequiredArgsConstructor;
import opennlp.tools.doccat.*;
import opennlp.tools.util.*;
import opennlp.tools.util.model.ModelUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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
    public ChatbotAnswerer chatbotAnswerer(OrderRepository orderRepository, ItemRepository itemRepository) {
        return new OpenNlpBotAnswerer(
                categoryAnswers(),
                categoryModel(),
                orderRepository,
                itemRepository);
    }

    private DoccatModel categoryModel() {
        try {
            File file = new File("category.txt");
            InputStreamFactory inputStreamFactory = new MarkableFileInputStreamFactory(file);
            ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, StandardCharsets.UTF_8);
            ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);
            DoccatFactory factory = new DoccatFactory(new FeatureGenerator[]{new BagOfWordsFeatureGenerator()});
            TrainingParameters params = ModelUtil.createDefaultTrainingParameters();
            params.put(TrainingParameters.CUTOFF_PARAM, 0);
            return DocumentCategorizerME.train("en", sampleStream, params, factory);
        } catch (IOException e) {
            throw new RuntimeException("can't get category model");
        }
    }

    private Map<String, String> categoryAnswers() {
        Map<String, String> answers = new HashMap<>();
        answers.put("default", "Sorry, I didn’t understand - could you please rephrase?");
        answers.put("greeting", "Hello! How can i help you?");
        answers.put("continue", "Want something else?");
        answers.put("complete", "Nice to meet you.");
        answers.put("product-pizza", "Take your favorite PIZZA!");
        answers.put("product-lunch", "You order lunch");
        answers.put("order-show", "this is your order");
        answers.put("product-pizza-cancel", "pizza cancelled");
        answers.put("product-lunch-cancel", "lunch cancelled");
        return answers;
    }
}
