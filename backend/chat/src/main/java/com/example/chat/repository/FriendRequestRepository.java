package com.example.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chat.model.FriendRequest;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    
   

}
