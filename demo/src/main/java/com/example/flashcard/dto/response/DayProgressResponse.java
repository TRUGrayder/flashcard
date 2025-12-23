package com.example.flashcard.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DayProgressResponse {
    private Integer day;
    private long totalWords;
    private long masteredWords;
    @JsonProperty("isUnlocked")
    private boolean isUnlocked; // True = Mở khóa, False = Khóa
}