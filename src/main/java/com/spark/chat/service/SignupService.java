package com.spark.chat.service;

import org.springframework.stereotype.Service;
import com.spark.chat.dto.SignupRequest;
import com.spark.chat.dto.SignupResponse;
import com.spark.chat.entity.User;
import com.spark.chat.repository.UserRepository;

@Service
public class SignupService {

    private final UserRepository userRepository;

    public SignupService(UserRepository userRepository) {
        this.userRepository = userRepository; // Fixed: removed the extra semicolon
    }

    public SignupResponse signup(SignupRequest req) {
        // Checking if mobile exists
        if (userRepository.findByMobile(req.mobile).isPresent()) {
            return new SignupResponse(false, "mobile", "Mobile exists", null, null, 0, null);
        }

        User user = new User();
        user.setName(req.name);
        user.setMobile(req.mobile);
        user.setPassword(req.password);
        user.setAge(req.age);
        user.setGender(req.gender);

        userRepository.save(user);

        return new SignupResponse(true, null, "Success", null, user.getName(), user.getAge(), user.getGender());
    }
}
