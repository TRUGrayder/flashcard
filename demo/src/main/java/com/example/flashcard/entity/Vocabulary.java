package com.example.flashcard.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "vocabulary")
@Data
public class Vocabulary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String word;

    @Column(columnDefinition = "TEXT")
    private String meaning;

    private String pronunciation;

    @Column(columnDefinition = "TEXT")
    private String example;

    @Column(name = "day_plan")
    private Integer dayPlan;

    private Integer status; // 0: New, 1: Mastered
    @Column(name = "part_of_speech")
    private String partOfSpeech;
}