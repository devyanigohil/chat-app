package com.example.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.chat.model.FriendRequest;
import com.example.chat.model.User;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    @Query(value = "SELECT * FROM friend_request WHERE receiver_id = (SELECT id FROM users WHERE username = ?1) AND handled = false", nativeQuery = true)
    List<FriendRequest> findPendingRequests(String username);
    
    @Query(value = "SELECT * FROM users u " +
        "WHERE u.username LIKE CONCAT('%', ?1, '%') " +
        "AND u.username <> ?2 " +
        "AND u.id NOT IN (" +
        "  SELECT CASE " +
        "    WHEN fr.sender_id = ?3 THEN fr.receiver_id " +
        "    ELSE fr.sender_id " +
        "  END " +
        "  FROM friend_request fr " +
        "  WHERE (fr.sender_id = ?3 OR fr.receiver_id = ?3) " +
        "    AND fr.handled = true AND fr.accepted = true" +
        ")", nativeQuery = true)
    List<User> searchForFriendRequest(String query, String currentUsername, Long currentUserId);
   

}
