package com.example.chat.mapper;

import com.example.chat.dto.FriendRequestDTO;
import com.example.chat.model.FriendRequest;
import com.example.chat.model.User;

public class FriendRequestMapper {

    public static FriendRequestDTO toDTO(FriendRequest entity) {
        FriendRequestDTO dto = new FriendRequestDTO();
        dto.setReceiver(entity.getReceiver() != null ? entity.getReceiver().getUsername() : null);
        dto.setMsg(entity.getMessage());
        dto.setSender(entity.getSender() != null ? entity.getSender().getUsername() : null);
        dto.setId(entity.getId());
        return dto;
    }

    public static FriendRequest toEntity(FriendRequestDTO dto, User sender, User receiver) {
        FriendRequest entity = new FriendRequest();
        entity.setSender(sender);
        entity.setReceiver(receiver);
        entity.setMessage(dto.getMsg());
        return entity;
    }   
}