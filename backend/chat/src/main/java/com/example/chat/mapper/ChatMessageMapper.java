package com.example.chat.mapper;

import com.example.chat.dto.ChatMessageRequestDTO;
import com.example.chat.dto.ChatMessageResponseDTO;
import com.example.chat.model.ChatMessage;

public class ChatMessageMapper {

    public static ChatMessage toModel(ChatMessageRequestDTO msg){
        ChatMessage messge=new ChatMessage();
        messge.setContent(msg.getContent());
        return messge;
    }

    public static ChatMessageResponseDTO toDto(ChatMessage msg, String sender){
        ChatMessageResponseDTO msgres=new ChatMessageResponseDTO();
        msgres.setContent(msg.getContent());
         msgres.setSender(sender);
        // msgres.setRoom(msg.getRoom());

        return msgres;
    }

}
