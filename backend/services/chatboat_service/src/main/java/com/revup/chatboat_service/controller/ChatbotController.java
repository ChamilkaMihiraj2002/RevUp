package com.revup.chatboat_service.controller;

import com.revup.chatboat_service.service.ExternalApiService;
import com.revup.chatboat_service.service.KnowledgeBaseService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
public class ChatbotController {
    private final ChatClient chatClient;
    private final ExternalApiService externalApiService;
    private final KnowledgeBaseService knowledgeBaseService;

    public ChatbotController(ChatClient.Builder builder, ExternalApiService externalApiService, KnowledgeBaseService knowledgeBaseService) {
        this.chatClient = builder.build();
        this.externalApiService = externalApiService;
        this.knowledgeBaseService = knowledgeBaseService;
    }

    /**
     * Chat endpoint which queries an external microservice for contextual data
     * and forwards that data to the AI chat client so the model can answer
     * based on the external data.
     *
     * Example: GET /api/v1/chat?q=Tell+me+about+product+X
     */
    @GetMapping("api/v1/chat")
    public Mono<ChatResponse> chat(@RequestParam(value = "q", required = false, defaultValue = "Tell me an interesting fact about Google gemini") String question) {
        String kbAnswer = knowledgeBaseService.answer(question);
        if (kbAnswer != null && !kbAnswer.isBlank()) {
            StringBuilder prompt = new StringBuilder();
            prompt.append("SYSTEM: You are an expert assistant for a vehicle service website. Use the following knowledge base to answer the user's question.\n\n");
            prompt.append("Knowledge Base:\n");
            prompt.append(kbAnswer).append("\n\n");
            prompt.append("USER QUESTION: ").append(question);
            ChatResponse response = chatClient.prompt()
                .user(prompt.toString())
                .call()
                .chatResponse();
            return Mono.just(response);
        }
        // Fallback to external API as before
        return externalApiService.fetchRawJson(question)
            .flatMap(rawJson -> {
                String summary = null;
                try {
                    summary = externalApiService.summarizeJson(rawJson);
                } catch (Exception ignore) {
                    summary = null;
                }

                StringBuilder prompt = new StringBuilder();
                prompt.append("SYSTEM: You are an expert assistant for a vehicle service website. ONLY use the external data provided below to answer questions about services, prices, durations, codes, or descriptions. If the answer is not in the data, reply: 'Sorry, I don't have that information.' Do NOT use your general knowledge. Do NOT invent or guess values.\n\n");

                if (rawJson != null && !rawJson.isBlank()) {
                    prompt.append("External data (JSON):\n");
                    prompt.append(rawJson).append("\n\n");
                    if (summary != null && !summary.isBlank()) {
                        prompt.append("Short summary:\n");
                        prompt.append(summary).append("\n\n");
                    }
                } else {
                    prompt.append("No external data available. Answer: 'Sorry, I don't have that information.'\n\n");
                }

                prompt.append("USER QUESTION: ").append(question);

                ChatResponse response = chatClient.prompt()
                    .user(prompt.toString())
                    .call()
                    .chatResponse();
                return Mono.just(response);
            })
            .onErrorResume(ex -> {
                StringBuilder prompt = new StringBuilder();
                prompt.append("SYSTEM: You are an expert assistant for a vehicle service website. ONLY use the external data provided below to answer questions about services, prices, durations, codes, or descriptions. If the answer is not in the data, reply: 'Sorry, I don't have that information.' Do NOT use your general knowledge. Do NOT invent or guess values.\n\n");
                prompt.append("No external data available. Answer: 'Sorry, I don't have that information.'\n\n");
                prompt.append("USER QUESTION: ").append(question);
                ChatResponse response = chatClient.prompt()
                    .user(prompt.toString())
                    .call()
                    .chatResponse();
                return Mono.just(response);
            });
    }
}