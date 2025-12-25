package com.spark.chat.repository;

import com.spark.chat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Real-world use case: Check if a username is already taken
    // boolean existsByUsername(String username);
}
