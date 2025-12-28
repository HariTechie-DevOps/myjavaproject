package com.spark.chat.service;
import org.springframework.stereotype.Service;
import com.spark.chat.entity.User;
import com.spark.chat.repository.UserRepository;
import java.util.Optional;

@Service
public class SecurityService {
    private final UserRepository repo;

    public SecurityService(UserRepository repo) {
        this.repo = repo;
    }

    public boolean isTokenValid(String token) {
        if (token == null || token.isEmpty()) return false;
        // In a real-time app, we check if this token exists in our DB
        return repo.findAll().stream()
                .anyMatch(user -> token.equals(user.getToken()));
    }
}
