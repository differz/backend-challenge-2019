package com.differz.bc.chatbot;

import com.differz.bc.core.Item;
import com.differz.bc.core.Order;
import com.differz.bc.dao.ItemRepository;
import com.differz.bc.dao.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

import static com.differz.bc.chatbot.ChatbotService.BOT_KEY_PHRASE;

@Slf4j
@RequiredArgsConstructor
@Transactional
public class OpenNlpBotAnswerer implements ChatbotAnswerer {

    private final Map<String, String> answers;
    private final DoccatModel model;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final Order order = new Order();

    @Override
    public String getAnswer(String message) {
        return "";
    }

    @Override
    public String getAnswer(String message, UUID userId) {
        String answer = null;
        String msg = message.replace(BOT_KEY_PHRASE, "");
        Item pizza = itemRepository.findFirstByName("pizza").orElseThrow();
        Item lunch = itemRepository.findFirstByName("lunch").orElseThrow();

        try {
            String[] tokens = tokenize(msg);
            String[] posTags = detectPOSTags(tokens);
            String[] lemmas = lemmatize(tokens, posTags);
            String category = detectCategory(lemmas);
            answer = answers.get(category);
            // detectTime(tokens);
            // detectDate(tokens);

            if (category.equals("product-pizza")) {
                order.getItems().add(pizza);
                order.setUserId(userId);
                orderRepository.save(order);
            }
            if (category.equals("product-lunch")) {
                order.getItems().add(lunch);
                order.setUserId(userId);
                orderRepository.save(order);
            }
            if (category.equals("product-pizza-cancel")) {
                order.getItems().remove(pizza);
                order.setUserId(userId);
                orderRepository.save(order);
            }
            if (category.equals("product-lunch-cancel")) {
                order.getItems().remove(lunch);
                order.setUserId(userId);
                orderRepository.save(order);
            }
            if (category.equals("order-show")) {
                answer = answer + "->" + order.getItems();
            }


        } catch (IOException e) {
            log.error("can't get answer", e);
        }
        if (answer == null) {
            answer = "i don't understand";
        }
        return answer;
    }

    private InputStream getModelInputStream(String resource) {
        return getClass().getClassLoader().getResourceAsStream(resource);
    }

    String[] tokenize(String sentence) throws IOException {
        try (InputStream modelIn = getModelInputStream("models/en-token.bin")) {
            TokenizerME tokenizerME = new TokenizerME(new TokenizerModel(modelIn));
            String[] tokens = tokenizerME.tokenize(sentence);
            log.info("Tokenizer : " + String.join(" | ", tokens));
            return tokens;
        }
    }

    private String[] detectPOSTags(String[] tokens) throws IOException {
        try (InputStream modelIn = getModelInputStream("models/en-pos-maxent.bin")) {
            POSTaggerME posTaggerME = new POSTaggerME(new POSModel(modelIn));
            String[] posTokens = posTaggerME.tag(tokens);
            log.info("POS Tags : " + String.join(" | ", posTokens));
            return posTokens;
        }
    }

    private String[] lemmatize(String[] tokens, String[] posTags) throws IOException {
        try (InputStream modelIn = getModelInputStream("models/en-lemmatizer.bin")) {
            LemmatizerME lemmatizerME = new LemmatizerME(new LemmatizerModel(modelIn));
            String[] lemmaTokens = lemmatizerME.lemmatize(tokens, posTags);
            log.info("Lemmatizer : " + String.join(" | ", lemmaTokens));
            return lemmaTokens;
        }
    }

    private String detectCategory(String[] finalTokens) {
        DocumentCategorizerME categorizerME = new DocumentCategorizerME(model);
        double[] probabilitiesOfOutcomes = categorizerME.categorize(finalTokens);
        String category = categorizerME.getBestCategory(probabilitiesOfOutcomes);
        log.info("Category: " + category);
        return category;
    }

    String[] detectTime(String[] tokens) throws IOException {
        try (InputStream modelIn = getModelInputStream("models/en-ner-time.bin")) {
            TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
            NameFinderME nameFinder = new NameFinderME(model);
            Span[] nameSpans = nameFinder.find(tokens);
            String[] times = Span.spansToStrings(nameSpans, tokens);
            log.info("Time finder : " + String.join(" | ", times));
            return times;
        }
    }

    String[] detectDate(String[] tokens) throws IOException {
        try (InputStream modelIn = getModelInputStream("models/en-ner-date.bin")) {
            TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
            NameFinderME nameFinder = new NameFinderME(model);
            Span[] nameSpans = nameFinder.find(tokens);
            String[] dates = Span.spansToStrings(nameSpans, tokens);
            log.info("Date finder : " + String.join(" | ", dates));
            return dates;
        }
    }
}
