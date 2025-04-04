package com.example.ZenieTest.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/chatbot")
public class ChatController {

    private final String GEMINI_API_KEY = "AIzaSyCa6kLuelGrV8oQUFyKa5UikidEDr7UkN0"; // Replace with your API key

    @PostMapping
    public Map<String, String> chatWithJenie(@RequestBody Map<String, String> request) {
        String userMessage = request.get("userMessage");
        String aiResponse = getGeminiResponse(userMessage);

        Map<String, String> response = new HashMap<>();
        response.put("response", aiResponse);
        return response;
    }

    private String getGeminiResponse(String message) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + GEMINI_API_KEY;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(Map.of("parts", List.of(Map.of("text", message)))));

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.postForObject(url, requestBody, Map.class);

        try {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                if (parts != null && !parts.isEmpty()) {
                    return (String) parts.get(0).get("text");
                }
            }
        } catch (Exception e) {
            return "Oops! I couldn't process the response.";
        }

        return "Sorry, I couldn't generate a response.";    }
}
