package com.example.signup.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.signup.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);
    Optional<User> findByMobile(String mobile);
    Optional<User> findByPassword(String password);
}
