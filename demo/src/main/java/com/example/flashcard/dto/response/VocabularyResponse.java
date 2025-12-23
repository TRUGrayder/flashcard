package com.example.flashcard.dto.response;

import lombok.Data;

@Data
public class VocabularyResponse {
    private Integer id;
    private String word;
    private String meaning;
    private String pronunciation;
    private String example;
    private String partOfSpeech;
}