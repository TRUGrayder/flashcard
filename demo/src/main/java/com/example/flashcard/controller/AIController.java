package com.example.flashcard.controller;

import com.example.flashcard.common.ApiResponse;
import com.example.flashcard.service.impl.AIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/vocabularies/ai")
public class AIController {

    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/explain")
    public ResponseEntity<ApiResponse<String>> askAI(@RequestParam String word) {
        String result = aiService.getExplanation(word);
        return ResponseEntity.ok(ApiResponse.success(result, "Success"));
    }
}