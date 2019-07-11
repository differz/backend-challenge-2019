package com.differz.bc.chatbot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static com.differz.bc.chatbot.ChatbotService.BOT_KEY_PHRASE;

@Slf4j
@RequiredArgsConstructor
public class OpenNlpBotAnswerer implements ChatbotAnswerer {

    private final Map<String, String> answers;
    private final DoccatModel model;

    @Override
    public String getAnswer(String message) {
        String answer = null;
        String msg = message.replace(BOT_KEY_PHRASE, "");
        try {
            String[] tokens = tokenize(msg);
            String[] posTags = detectPOSTags(tokens);
            String[] lemmas = lemmatize(tokens, posTags);
            String category = detectCategory(lemmas);
            answer = answers.get(category);
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

    private String[] tokenize(String sentence) throws IOException {
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
}
