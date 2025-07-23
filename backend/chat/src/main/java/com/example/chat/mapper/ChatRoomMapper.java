package com.example.chat.mapper;

import java.security.Principal;

import com.example.chat.dto.ChatRoomDTO;
import com.example.chat.model.ChatRoom;
import com.example.chat.repository.UserRepository;

public class ChatRoomMapper {

    public static ChatRoom toModel(ChatRoomDTO chatRoomDTO){
        ChatRoom chatroom=new ChatRoom();
        chatroom.setDescription(chatRoomDTO.getDescription());
        chatroom.setName(chatRoomDTO.getName());
        return chatroom;
    }

    public static ChatRoomDTO toDto(ChatRoom chatRoom, Principal principal, UserRepository userRepo){
        ChatRoomDTO chatRoomDTO=new ChatRoomDTO();
        chatRoomDTO.setDescription(chatRoom.getDescription());
        chatRoomDTO.setId(chatRoom.getId());
        if(!chatRoom.getType().equals(ChatRoom.ChatRoomType.PRIVATE)){
            chatRoomDTO.setName(chatRoom.getName());
            chatRoomDTO.setAdmin(chatRoom.getCreatedby().equals(userRepo.findByUsername(principal.getName())));
        }else{
            chatRoomDTO.setName(chatRoom.getParticipants().stream()
                .filter(user -> !user.getUsername().equals(principal.getName()))
                .findFirst()
                .map(user -> user.getUsername())
                .orElse("Private Chat"));
            chatRoomDTO.setAdmin(false);
        }
        return chatRoomDTO;
    }
}
