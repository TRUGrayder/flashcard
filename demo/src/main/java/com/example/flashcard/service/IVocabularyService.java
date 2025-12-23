package com.example.flashcard.service;

import com.example.flashcard.dto.response.DayProgressResponse;
import com.example.flashcard.dto.response.QuizResponse;
import com.example.flashcard.dto.response.VocabularyResponse;
import java.util.List;

public interface IVocabularyService {

    // 👇 HÀM CHÍNH (Đã cập nhật): Nhận 3 tham số (day, random, includeAll)
    List<VocabularyResponse> getDailyWords(Integer day, boolean isRandom, boolean includeAll);

    void markAsMastered(Integer id);

    // Dashboard: Lấy danh sách 30 ngày
    List<DayProgressResponse> getAllDaysProgress();

    // Reset: Học lại từ đầu
    void resetDay(Integer day);

    // Quiz: Tạo đề thi
    List<QuizResponse> generateQuiz(Integer day);

    // Hoàn thành: Đánh dấu thuộc hết cả ngày
    void completeDay(Integer day);
}