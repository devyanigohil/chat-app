package com.example.chat.service;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.chat.dto.ChatMessageRequestDTO;
import com.example.chat.dto.ChatMessageResponseDTO;
import com.example.chat.mapper.ChatMessageMapper;
import com.example.chat.model.ChatMessage;
import com.example.chat.model.ChatRoom;
import com.example.chat.model.User;
import com.example.chat.repository.ChatMessageRepository;
import com.example.chat.repository.ChatRoomRepository;
import com.example.chat.repository.UserRepository;

@Service
public class ChatService {
        private final ChatMessageRepository repository;
        private final ChatRoomRepository chatRoomRepo;
        private final UserRepository userRepo;

        public ChatService(ChatMessageRepository repository,ChatRoomRepository chatRoomRepository, UserRepository userRepo) {
            this.repository = repository;
            this.chatRoomRepo=chatRoomRepository;
            this.userRepo=userRepo;
        }
        
        public ChatMessageResponseDTO saveMessage(ChatMessageRequestDTO msg, long roomId,Principal principal){

            ChatMessage msgmodel=ChatMessageMapper.toModel(msg);
            ChatRoom room=chatRoomRepo.findById(roomId).orElseThrow(()->new RuntimeException("Chat Room not found!"));
            User sender=userRepo.findByUsername(principal.getName());
            msgmodel.setRoom(room);
            msgmodel.setSender(sender);
            msgmodel.setTimestamp( LocalDateTime.now());
            repository.save(msgmodel);

            return ChatMessageMapper.toDto(msgmodel,sender.getUsername());

        }

}
