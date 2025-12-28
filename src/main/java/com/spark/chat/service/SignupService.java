package com.spark.chat.service;

import java.util.UUID;
import org.springframework.stereotype.Service;
import com.spark.chat.dto.SignupRequest;
import com.spark.chat.dto.SignupResponse;
import com.spark.chat.entity.User;
import com.spark.chat.repository.UserRepository;

@Service
public class SignupService {

    private final UserRepository repo;

    public SignupService(UserRepository repo) {
        this.repo = repo;
    }

    public SignupResponse register(SignupRequest req) {
    // Change req.getMobile() to req.mobile
    if (userRepository.findByMobile(req.mobile).isPresent()) {
        return new SignupResponse(false, "mobile", "Mobile already exists", null, null, 0, null);
    }

    User user = new User();
    // Remove the "get" and parentheses from all lines below
    user.setName(req.name);
    user.setAge(req.age);
    user.setGender(req.gender);
    user.setMobile(req.mobile);
    user.setPassword(req.password);
    
    userRepository.save(user);

    return new SignupResponse(true, null, "User registered successfully", null, user.getName(), user.getAge(), user.getGender());
}
}
