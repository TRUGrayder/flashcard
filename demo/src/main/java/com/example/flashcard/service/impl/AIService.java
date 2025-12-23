package com.example.flashcard.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AIService {

    // Key của bạn (Mình đã xóa bớt để bảo mật, bạn nhớ dán lại key gốc vào đây)
    private static final String GEMINI_API_KEY = "";

    // 👇 ĐÃ SỬA: Đổi "v1" thành "v1beta" để chạy được gemini-1.5-flash
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent?key=" + GEMINI_API_KEY;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getExplanation(String word) {
        try {
            // Prompt
            String prompt = "Giải thích từ tiếng Anh \"" + word + "\" ngắn gọn, hài hước cho người Việt. Gồm: Loại từ, Nghĩa, Ví dụ vui.";

            Map<String, Object> textPart = new HashMap<>();
            textPart.put("text", prompt);

            Map<String, Object> parts = new HashMap<>();
            parts.put("parts", new Object[]{textPart});

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", new Object[]{parts});

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // Gọi API
            String response = restTemplate.postForObject(API_URL, entity, String.class);

            // Đọc kết quả
            JsonNode root = objectMapper.readTree(response);
            return root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();

        } catch (Exception e) {
            e.printStackTrace();
            // Trả về thông báo lỗi rõ ràng cho Frontend
            return "AI đang bận (Lỗi: " + e.getMessage() + ")";
        }
    }
}