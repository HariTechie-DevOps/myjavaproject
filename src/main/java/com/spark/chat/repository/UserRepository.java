package com.spark.chat.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.spark.chat.entity.User;

    @Repository
    public interface UserRepository extends JpaRepository<User, Long> {
        Optional<User> findByMobile(String mobile);
    
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.mobile = :mobile")
    void updatePassword(String mobile, String newPassword);
}
