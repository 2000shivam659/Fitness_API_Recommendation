package com.project.fitness.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.fitness.model.Activity;
import com.project.fitness.model.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAIService {

    private final RestClient openAiRestClient;
    private final ObjectMapper objectMapper;

    @Value("${openai.model:gpt-4o-mini}")
    private String model;

    @Data
    public static class AiRecommendationResult {
        private String type;
        private String recommendation;
        private List<String> improvements = new ArrayList<>();
        private List<String> suggestions = new ArrayList<>();
        private List<String> safety = new ArrayList<>();
    }

    public AiRecommendationResult generateRecommendation(User user, Activity activity) {
        String systemPrompt = """
            You are a professional fitness and wellness AI recommendation engine.
            Analyze the provided user and workout activity details to generate a comprehensive, structured recommendation.
            Respond strictly with a JSON object matching this schema:
            {
              "type": "Workout Analysis",
              "recommendation": "A detailed 2-3 sentence personalized recommendation text.",
              "improvements": ["Point 1", "Point 2"],
              "suggestions": ["Suggestion 1", "Suggestion 2"],
              "safety": ["Safety tip 1", "Safety tip 2"]
            }
            Do not include any markdown formatting, markdown blocks, or text outside the raw JSON object.
            """;

        String userPrompt = String.format(
            "User: %s %s\nActivity Type: %s\nDuration: %d minutes\nCalories Burned: %d\nAdditional Info: %s",
            user.getFirstName() != null ? user.getFirstName() : "User",
            user.getLastName() != null ? user.getLastName() : "",
            activity.getActivityType() != null ? activity.getActivityType().name() : "General Workout",
            activity.getDuration() != null ? activity.getDuration() : 0,
            activity.getCaloriesBurned() != null ? activity.getCaloriesBurned() : 0,
            activity.getAdditionalActivity() != null ? activity.getAdditionalActivity().toString() : "None"
        );

        Map<String, Object> requestBody = Map.of(
            "model", model,
            "messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userPrompt)
            ),
            "response_format", Map.of("type", "json_object")
        );

        try {
            String responseString = openAiRestClient.post()
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            log.info("Received OpenAI response: {}", responseString);

            JsonNode rootNode = objectMapper.readTree(responseString);
            JsonNode choices = rootNode.path("choices");
            if (choices.isArray() && !choices.isEmpty()) {
                String content = choices.get(0).path("message").path("content").asText();
                return objectMapper.readValue(content, AiRecommendationResult.class);
            }
            throw new RuntimeException("Empty choices in OpenAI response");
        } catch (Exception e) {
            log.error("Error generating AI recommendation from OpenAI: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate AI recommendation: " + e.getMessage(), e);
        }
    }
}
