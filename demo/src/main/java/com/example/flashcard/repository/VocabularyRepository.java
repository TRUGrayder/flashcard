package com.example.flashcard.repository;

import com.example.flashcard.entity.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary, Integer> {

    // 1. Lấy danh sách từ theo ngày
    List<Vocabulary> findByDayPlan(Integer dayPlan);

    // 2. Đếm tổng số từ trong 1 ngày (Để tính progress)
    long countByDayPlan(Integer dayPlan);

    // 3. Đếm số từ đã thuộc trong 1 ngày
    long countByDayPlanAndStatus(Integer dayPlan, Integer status);

    // 4. Query lấy 3 đáp án sai ngẫu nhiên (Cho phần Quiz)
    // Logic: Lấy 3 từ khác id hiện tại, sắp xếp ngẫu nhiên, giới hạn 3
    @Query(value = "SELECT * FROM vocabulary WHERE id != :wordId ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<Vocabulary> findRandomWrongAnswers(Integer wordId);
}