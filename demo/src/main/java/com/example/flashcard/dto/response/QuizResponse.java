package com.example.flashcard.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class QuizResponse {
    private Integer wordId;
    private String question;      // Từ tiếng Anh (VD: Apple)
    private List<String> options; // 4 đáp án (1 đúng, 3 sai)
    private String correctAnswer; // Đáp án đúng (để so sánh)
}