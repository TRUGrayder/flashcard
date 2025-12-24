package com.example.flashcard.repository;

import com.example.flashcard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Hàm tìm user bằng tên đăng nhập (để check đăng nhập)
    Optional<User> findByUsername(String username);

    // Hàm kiểm tra user đã tồn tại chưa (để check đăng ký)
    boolean existsByUsername(String username);
}