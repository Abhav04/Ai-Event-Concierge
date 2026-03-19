package Ai.event.concierge.demo.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import jakarta.annotation.PostConstruct;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                // Just the base domain/path
                .baseUrl("https://generativelanguage.googleapis.com")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public JSONObject generateEvent(String userInput) {
        try {
            // Simplified prompt since we are using 'responseMimeType' below
            String prompt = """
You are an AI event planner.

Given a user request, generate a SPECIFIC event plan in JSON format.

Rules:
- Always suggest a REALISTIC and SPECIFIC venue (hotel, resort, or place)
- Do NOT return generic locations like "Goa"
- Include a proper venue name (e.g., "Taj Exotica Resort & Spa, Goa")
- Keep cost realistic based on group size and duration

Return STRICT JSON:
{
  "venue_name": "",
  "location": "",
  "estimated_cost": "",
  "why_it_fits": ""
}

Input: %s
""".formatted(userInput);

            // 1. Build Request Body
            JSONObject requestBody = new JSONObject();

            // Contents
            JSONObject textPart = new JSONObject().put("text", prompt);
            JSONObject content = new JSONObject().put("parts", new JSONArray().put(textPart));
            requestBody.put("contents", new JSONArray().put(content));

            // 2. Force JSON Mode (2026 Best Practice)
            JSONObject generationConfig = new JSONObject();
            generationConfig.put("responseMimeType", "application/json");
            requestBody.put("generationConfig", generationConfig);

            // 3. Make the Call
            String response = webClient.post()
                    .uri("/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey)
                    .bodyValue(requestBody.toString())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            // 4. Parse Response
            JSONObject jsonResponse = new JSONObject(response);
            String rawText = jsonResponse
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");

            // Because of responseMimeType, rawText is guaranteed to be pure JSON
            return new JSONObject(rawText);

        } catch (org.springframework.web.reactive.function.client.WebClientResponseException e) {
            System.err.println("API Error Body: " + e.getResponseBodyAsString());
            throw new RuntimeException("Gemini API rejected request: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Internal Error", e);
        }
    }
}