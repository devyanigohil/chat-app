package com.example.chat.service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.chat.dto.ChatMessageResponseDTO;
import com.example.chat.dto.ChatRoomDTO;
import com.example.chat.mapper.ChatMessageMapper;
import com.example.chat.mapper.ChatRoomMapper;
import com.example.chat.model.ChatRoom;
import com.example.chat.model.User;
import com.example.chat.repository.ChatRoomRepository;
import com.example.chat.repository.UserRepository;

@Service
public class ChatRoomService {


    private final UserRepository userRepository;

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomService(UserRepository userRepository, ChatRoomRepository chatRoomRepository) {
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    public ChatRoomDTO createRoom(Principal principal,ChatRoomDTO chatRoomDTO)
    {
        User user=userRepository.findByUsername(principal.getName());
        ChatRoom room=ChatRoomMapper.toModel(chatRoomDTO);
        room.setCreatedby(user);
        room.setAdmin(user);
        room.setCreateddate(LocalDateTime.now());
        room.getParticipants().add(user);
        room.setType(ChatRoom.ChatRoomType.GROUP);
        chatRoomRepository.save(room);
        return ChatRoomMapper.toDto(room,principal,userRepository);
    }


    public List<ChatRoomDTO> getByCreatedBy(Principal principal){
        User user=userRepository.findByUsername(principal.getName());
        List<ChatRoom> rooms=chatRoomRepository.findByParticipantsContaining(user);
        return rooms.stream()
                     .map(room -> ChatRoomMapper.toDto(room,principal,userRepository))
                    .collect(Collectors.toList());
    }

    public List<ChatMessageResponseDTO> getMessages(String roomid) {
        ChatRoom room = chatRoomRepository.findById(Long.parseLong(roomid))
                .orElseThrow(() -> new RuntimeException("Room not found"));
        return room.getMessages().stream()
                .map(message -> ChatMessageMapper.toDto(message))
                .collect(Collectors.toList());
    }
    // public ChatRoomDTO addUserToRoom(String roomname, String username){
    //     ChatRoom room=chatRoomRepository.findByName(roomname);
    //     User user=userRepository.findByUsername(username);

    //     room.getParticipants().add(user);
    //     return ChatRoomMapper.toDto(room);
    // }
}
