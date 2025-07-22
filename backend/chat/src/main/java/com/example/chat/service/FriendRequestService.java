package com.example.chat.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.chat.dto.FriendRequestDTO;
import com.example.chat.model.FriendRequest;
import com.example.chat.model.User;
import com.example.chat.repository.FriendRequestRepository;
import com.example.chat.repository.UserRepository;

@Service
public class FriendRequestService {

    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;  
    
    public FriendRequestService(UserRepository userRepository, FriendRequestRepository friendRequestRepository) {
        this.userRepository = userRepository;
        this.friendRequestRepository = friendRequestRepository;
    }
    
    public void sendRequest(String fromUsername, FriendRequestDTO requestDTO) {
       User fromUser = userRepository.findByUsername(fromUsername);
        if (fromUser == null) {
            throw new RuntimeException("User not found: " + fromUsername);
        }
        User toUser = userRepository.findByUsername(requestDTO.getReceiver());
        if (toUser == null) {
            throw new RuntimeException("User not found: " + requestDTO.getReceiver());
        }
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(fromUser);
        friendRequest.setReceiver(toUser);
        friendRequest.setHandled(false);
        friendRequest.setMessage(requestDTO.getMsg());
        friendRequest.setRequestedAt(LocalDateTime.now());
        friendRequestRepository.save(friendRequest);
    }

    public void handleRequest(Long id, String username) {
        // Logic to handle a friend request (accept or reject)
        // This could involve updating the status of the FriendRequest entity
    }

    public List<FriendRequestDTO> getPendingRequests(String username) {
       List<FriendRequest> requests = friendRequestRepository.findPendingRequests(username);
        return new ArrayList<FriendRequestDTO>();
    }
}
