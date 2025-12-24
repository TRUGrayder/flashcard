package com.example.flashcard.controller;

import com.example.flashcard.dto.request.AuthRequest;
import com.example.flashcard.entity.User;
import com.example.flashcard.repository.UserRepository;
import com.example.flashcard.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    // 1. API Đăng Ký
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Lỗi: Tên đăng nhập đã tồn tại!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        // Mã hóa mật khẩu trước khi lưu
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setRole("USER"); // Mặc định là USER thường

        userRepository.save(user);

        return ResponseEntity.ok("Đăng ký thành công! Hãy đăng nhập ngay.");
    }

    // 2. API Đăng Nhập
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        // Xác thực username/password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Nếu đúng thì tạo Token
        if (authentication.isAuthenticated()) {
            String token = jwtUtils.generateToken(request.getUsername());

            // Trả về Token cho Frontend dùng
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "Đăng nhập thành công!");

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("Sai thông tin đăng nhập!");
        }
    }
}