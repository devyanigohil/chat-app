package com.example.chat.service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.chat.dto.FriendRequestActionDTO;
import com.example.chat.dto.FriendRequestDTO;
import com.example.chat.mapper.FriendRequestMapper;
import com.example.chat.model.ChatRoom;
import com.example.chat.model.FriendRequest;
import com.example.chat.model.User;
import com.example.chat.repository.ChatRoomRepository;
import com.example.chat.repository.FriendRequestRepository;
import com.example.chat.repository.UserRepository;

@Service
public class FriendRequestService {

    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;  
    private final ChatRoomRepository   chatRoomRepository;
    
    public FriendRequestService(UserRepository userRepository, FriendRequestRepository friendRequestRepository, 
                                ChatRoomRepository chatRoomRepository) {
        this.userRepository = userRepository;
        this.friendRequestRepository = friendRequestRepository;
        this.chatRoomRepository = chatRoomRepository;
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

    public void handleRequest(FriendRequestActionDTO requestDto, Principal principal) {
        FriendRequest friendRequest = friendRequestRepository.findById(requestDto.getRequestId())
                .orElseThrow(() -> new RuntimeException("Friend request not found"));
        if (!friendRequest.getReceiver().equals(userRepository.findByUsername(principal.getName()))) {
            throw new RuntimeException("You are not authorized to handle this request");    
        }
        if (requestDto.isApprove()) {
            friendRequest.setHandled(true);
            friendRequest.setAccepted(true);
            friendRequest.setRejected(false);
            friendRequest.setRespondedAt(LocalDateTime.now());
            ChatRoom privateChatRoom = findPrivateChatRoom(friendRequest.getSender(), friendRequest.getReceiver());
            if (privateChatRoom == null) {
                ChatRoom newChatRoom = new ChatRoom();
                newChatRoom.setType(ChatRoom.ChatRoomType.PRIVATE);
                newChatRoom.setParticipants(new HashSet<>(List.of(friendRequest.getSender(), friendRequest.getReceiver())));
                newChatRoom.setCreateddate(LocalDateTime.now() );
                chatRoomRepository.save(newChatRoom);
            }
        } else {
            friendRequest.setAccepted(false);
            friendRequest.setRejected(true);
            friendRequest.setHandled(true);
            friendRequest.setRespondedAt(LocalDateTime.now());
            
        }
         friendRequestRepository.save(friendRequest);
    }

    public List<FriendRequestDTO> getPendingRequests(String username) {
       List<FriendRequest> requests = friendRequestRepository.findPendingRequests(username);
        return requests.stream().map(FriendRequestMapper::toDTO).toList();
    }

    private ChatRoom findPrivateChatRoom(User user1, User user2) {
         return chatRoomRepository.findPrivateRoomByParticipants(user1.getId(), user2.getId());
    }
}
