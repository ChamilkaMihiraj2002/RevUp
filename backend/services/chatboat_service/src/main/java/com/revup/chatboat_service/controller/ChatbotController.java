package com.revup.chatboat_service.controller;

import com.revup.chatboat_service.service.ExternalApiService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ChatbotController {
    private final ChatClient chatClient;
    private final ExternalApiService externalApiService;

    public ChatbotController(ChatClient.Builder builder, ExternalApiService externalApiService) {
        this.chatClient = builder.build();
        this.externalApiService = externalApiService;
    }

    /**
     * Chat endpoint which queries an external microservice for contextual data
     * and forwards that data to the AI chat client so the model can answer
     * based on the external data.
     *
     * Example: GET /api/v1/chat?q=Tell+me+about+product+X
     */
    @GetMapping("api/v1/chat")
    public ChatResponse chat(@RequestParam(value = "q", required = false, defaultValue = "Tell me an interesting fact about Google gemini") String question) {
        // Fetch contextual data from the other microservice. This is optional: if the
        // external service fails or returns nothing we still call the chat client.
        String externalData = null;
        try {
            externalData = externalApiService.fetchData(question);
        } catch (Exception ex) {
            // Log if you have logging. For now, we fall back to no external data.
            // System.err.println("Failed to fetch external data: " + ex.getMessage());
        }

        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a helpful assistant.\n");
        if (externalData != null && !externalData.isBlank()) {
            prompt.append("Use the following external data to answer the question.\n");
            prompt.append("External data:\n");
            prompt.append(externalData).append("\n\n");
        } else {
            prompt.append("No external data available. Answer using your knowledge.\n\n");
        }
        prompt.append("Question: ").append(question);

    return chatClient.prompt()
        .user(prompt.toString())
        .call()
        .chatResponse();
    }
}