package com.spark.chat.service;

import org.springframework.stereotype.Service;
import com.spark.chat.dto.SignupRequest;
import com.spark.chat.dto.SignupResponse;
import com.spark.chat.entity.User;
import com.spark.chat.repository.UserRepository;

@Service
public class SignupService {

    // 1. THIS LINE IS CRITICAL. It must be outside the constructor.
    private final UserRepository userRepository;

    // 2. The constructor just "fills" the variable we created above
    public SignupService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 3. Now this method can "see" userRepository
    public SignupResponse signup(SignupRequest req) {
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
