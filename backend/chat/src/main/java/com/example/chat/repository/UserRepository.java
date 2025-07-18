package com.example.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.chat.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    @Query(value = "SELECT * FROM users WHERE username LIKE CONCAT('%', ?1, '%') and username <> ?2" ,nativeQuery = true)
    List<User> getByUsername(String query,String username);

}
