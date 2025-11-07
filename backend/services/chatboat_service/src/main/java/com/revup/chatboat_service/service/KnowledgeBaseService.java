package com.revup.chatboat_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class KnowledgeBaseService {
    private final ObjectMapper mapper = new ObjectMapper();
    private JsonNode root;

    public KnowledgeBaseService() {
        try {
            ClassPathResource resource = new ClassPathResource("knowledge_base.json");
            root = mapper.readTree(resource.getInputStream());
        } catch (IOException e) {
            root = null;
        }
    }

    public List<String> getServices() {
        List<String> result = new ArrayList<>();
        if (root != null && root.has("services")) {
            for (JsonNode node : root.get("services")) {
                result.add(node.asText());
            }
        }
        return result;
    }

    public List<String> getWebsiteUsage() {
        List<String> result = new ArrayList<>();
        if (root != null && root.has("website_usage")) {
            for (JsonNode node : root.get("website_usage")) {
                result.add(node.asText());
            }
        }
        return result;
    }

    public String answer(String question) {
        String q = question.toLowerCase();
        if (q.contains("service") || q.contains("offer")) {
            return String.join("\n", getServices());
        }
        if (q.contains("website") || q.contains("how to use") || q.contains("help")) {
            return String.join("\n", getWebsiteUsage());
        }
        return null;
    }
}
