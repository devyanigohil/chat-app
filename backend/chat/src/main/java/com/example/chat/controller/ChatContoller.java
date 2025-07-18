package com.example.chat.controller;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.chat.dto.ChatMessageRequestDTO;
import com.example.chat.service.ChatService;

@Controller
public class ChatContoller {

    private final ChatService chatService;
    
    private final SimpMessagingTemplate messagingTemplate;


    public ChatContoller(ChatService chatService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }




    @MessageMapping("/room/{roomId}")
    public void handleMessage(@DestinationVariable long roomId, ChatMessageRequestDTO msg, Principal principal){
        chatService.saveMessage(msg,roomId,principal);
        messagingTemplate.convertAndSend("/topic/room/"+roomId, msg);
    }

}
