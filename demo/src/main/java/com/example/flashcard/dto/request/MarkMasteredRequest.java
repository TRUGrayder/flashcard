package com.example.flashcard.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MarkMasteredRequest {
    @NotNull(message = "ID không được để trống")
    private Integer id;
}