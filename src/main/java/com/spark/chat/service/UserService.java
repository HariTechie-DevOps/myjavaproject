package com.spark.chat.service;

import com.spark.chat.entity.User;
import com.spark.chat.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public User setLanguage(Long id, String lang) {
        User u = repo.findById(id)
                     .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        u.setPreferredLanguage(lang.toLowerCase()); // Normalize to lowercase
        return repo.save(u);
    }

    public User get(Long id) {
        return repo.findById(id)
                   .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
}
