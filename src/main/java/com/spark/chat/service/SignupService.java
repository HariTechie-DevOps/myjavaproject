package com.spark.chat.service;

import java.util.UUID;
import org.springframework.stereotype.Service;
import com.example.signup.dto.SignupRequest;
import com.example.signup.dto.SignupResponse;
import com.example.signup.entity.User;
import com.example.signup.repository.UserRepository;

@Service
public class SignupService {

    private final UserRepository repo;

    public SignupService(UserRepository repo) {
        this.repo = repo;
    }

    public SignupResponse signup(SignupRequest req) {
        // Logic: Only Mobile needs to be strictly unique for account identity
        if (repo.findByMobile(req.getMobile()).isPresent()) {
            return new SignupResponse(false, "mobile", "Mobile number already registered", null, null, 0, null);
        }

        String token = UUID.randomUUID().toString();

        User user = new User();
        user.setName(req.getName());
        user.setAge(req.getAge());
        user.setGender(req.getGender());
        user.setMobile(req.getMobile());
        user.setPassword(req.getPassword());
        user.setToken(token);

        repo.save(user);

        return new SignupResponse(true, null, "Signup successful", token, user.getName(), user.getAge(), user.getGender());
    }
}
