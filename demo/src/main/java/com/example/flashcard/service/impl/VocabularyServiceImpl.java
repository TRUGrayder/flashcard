package com.example.flashcard.service.impl;

import com.example.flashcard.dto.response.DayProgressResponse;
import com.example.flashcard.dto.response.QuizResponse;
import com.example.flashcard.dto.response.VocabularyResponse;
import com.example.flashcard.entity.Vocabulary;
import com.example.flashcard.repository.VocabularyRepository;
import com.example.flashcard.service.IVocabularyService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VocabularyServiceImpl implements IVocabularyService {

    private final VocabularyRepository repository;

    public VocabularyServiceImpl(VocabularyRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<VocabularyResponse> getDailyWords(Integer day, boolean isRandom, boolean includeAll) {
        List<Vocabulary> entities = repository.findAll().stream()
                .filter(v -> v.getDayPlan().equals(day))
                .filter(v -> includeAll || v.getStatus() == 0) // 👈 QUAN TRỌNG: Nếu includeAll=true thì lấy hết
                .collect(Collectors.toList());

        if (isRandom) {
            Collections.shuffle(entities);
        }

        return entities.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void markAsMastered(Integer id) {
        repository.findById(id).ifPresent(v -> {
            v.setStatus(1);
            repository.save(v);
        });
    }

    // LOGIC TÍNH TOÁN MỞ KHÓA (GAMIFICATION)
    @Override
    public List<DayProgressResponse> getAllDaysProgress() {
        List<DayProgressResponse> list = new ArrayList<>();
        boolean previousDayCompleted = true; // Ngày 1 luôn mở

        for (int day = 1; day <= 30; day++) {
            long total = repository.countByDayPlan(day);
            if (total == 0) break; // Hết dữ liệu thì dừng

            long mastered = repository.countByDayPlanAndStatus(day, 1);
            boolean isCompleted = (total > 0 && total == mastered);

            list.add(DayProgressResponse.builder()
                    .day(day)
                    .totalWords(total)
                    .masteredWords(mastered)
                    .isUnlocked(previousDayCompleted) // Mở nếu ngày trước đã xong
                    .build());

            // Cập nhật trạng thái cho vòng lặp sau
            previousDayCompleted = isCompleted;
        }
        return list;
    }

    // LOGIC RESET HỌC LẠI
    @Override
    public void resetDay(Integer day) {
        List<Vocabulary> words = repository.findByDayPlan(day);
        for (Vocabulary word : words) {
            word.setStatus(0); // Reset về chưa thuộc
        }
        repository.saveAll(words);
    }

    private VocabularyResponse convertToDto(Vocabulary entity) {
        VocabularyResponse dto = new VocabularyResponse();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
    @Override
    public List<QuizResponse> generateQuiz(Integer day) {
        List<Vocabulary> words = repository.findByDayPlan(day);
        List<QuizResponse> quizList = new ArrayList<>();

        for (Vocabulary word : words) {
            // Lấy 3 đáp án sai từ DB
            List<Vocabulary> wrongWords = repository.findRandomWrongAnswers(word.getId());

            // Tạo danh sách 4 đáp án
            List<String> options = new ArrayList<>();
            options.add(word.getMeaning()); // Đáp án đúng
            for (Vocabulary w : wrongWords) options.add(w.getMeaning()); // 3 sai

            // Trộn ngẫu nhiên thứ tự đáp án
            Collections.shuffle(options);

            quizList.add(QuizResponse.builder()
                    .wordId(word.getId())
                    .question(word.getWord())
                    .correctAnswer(word.getMeaning())
                    .options(options)
                    .build());
        }
        // Trộn thứ tự câu hỏi
        Collections.shuffle(quizList);
        return quizList;
    }

    @Override
    public void completeDay(Integer day) {
        List<Vocabulary> words = repository.findByDayPlan(day);
        for (Vocabulary v : words) {
            v.setStatus(1); // Đánh dấu đã thuộc
        }
        repository.saveAll(words);
    }
}