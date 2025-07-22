package com.example.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.chat.model.FriendRequest;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    @Query(value = "SELECT * FROM friend_request WHERE receiver_id = (SELECT id FROM users WHERE username = ?1) AND handled = false", nativeQuery = true)
    List<FriendRequest> findPendingRequests(String username);
    
   

}
