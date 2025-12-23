package com.example.flashcard.controller;

import com.example.flashcard.common.ApiResponse;
import com.example.flashcard.dto.request.MarkMasteredRequest;
import com.example.flashcard.dto.response.DayProgressResponse;
import com.example.flashcard.dto.response.QuizResponse;
import com.example.flashcard.dto.response.VocabularyResponse;
import com.example.flashcard.service.IVocabularyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/v1/vocabularies", "/api/v1/vocabularies/"})
public class VocabularyController {

    private final IVocabularyService service;

    public VocabularyController(IVocabularyService service) {
        this.service = service;
    }

    // 👇 ĐÃ SỬA: Gộp lại thành 1 hàm duy nhất có đủ 3 tham số
    @GetMapping
    public ResponseEntity<ApiResponse<List<VocabularyResponse>>> getWords(
            @RequestParam(defaultValue = "1") Integer day,
            @RequestParam(defaultValue = "false") boolean random,
            @RequestParam(defaultValue = "false") boolean includeAll // Tham số mới để lấy cả từ đã thuộc
    ) {
        // Gọi xuống Service (Lưu ý: Bạn phải chắc chắn bên Service đã sửa hàm này nhận 3 tham số rồi nhé)
        List<VocabularyResponse> data = service.getDailyWords(day, random, includeAll);
        return ResponseEntity.ok(ApiResponse.success(data, "Success"));
    }

    // API: Lấy danh sách tiến độ các ngày (Dashboard)
    @GetMapping("/days")
    public ResponseEntity<ApiResponse<List<DayProgressResponse>>> getDaysStatus() {
        return ResponseEntity.ok(ApiResponse.success(service.getAllDaysProgress(), "Success"));
    }

    // API: Reset ngày học
    @PostMapping("/reset")
    public ResponseEntity<ApiResponse<Void>> resetDay(@RequestBody Map<String, Integer> payload) {
        service.resetDay(payload.get("day"));
        return ResponseEntity.ok(ApiResponse.success(null, "Đã reset ngày học"));
    }

    // API: Đánh dấu đã thuộc
    @PostMapping("/master")
    public ResponseEntity<ApiResponse<Void>> markMastered(@Valid @RequestBody MarkMasteredRequest request) {
        service.markAsMastered(request.getId());
        return ResponseEntity.ok(ApiResponse.success(null, "Marked as mastered"));
    }

    // API: Lấy đề thi
    @GetMapping("/quiz")
    public ResponseEntity<ApiResponse<List<QuizResponse>>> getQuiz(@RequestParam Integer day) {
        return ResponseEntity.ok(ApiResponse.success(service.generateQuiz(day), "Đã tạo đề thi"));
    }

    // API: Nộp bài (Hoàn thành ngày)
    @PostMapping("/complete-day")
    public ResponseEntity<ApiResponse<Void>> completeDay(@RequestBody Map<String, Integer> payload) {
        service.completeDay(payload.get("day"));
        return ResponseEntity.ok(ApiResponse.success(null, "Đã hoàn thành ngày học"));
    }
}