package com.example.chat.controller;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.chat.dto.ChatMessageRequestDTO;
import com.example.chat.dto.ChatMessageResponseDTO;
import com.example.chat.service.ChatService;
import com.example.chat.util.JwtUtil;

@Controller
public class ChatContoller {

    private final ChatService chatService;
    
    private final SimpMessagingTemplate messagingTemplate;

    private final JwtUtil jwtUtil;


    public ChatContoller(ChatService chatService, SimpMessagingTemplate messagingTemplate,JwtUtil jwtUtil) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
        this.jwtUtil = jwtUtil;
    }




    @MessageMapping("/room/{roomId}")
    public void handleMessage(@DestinationVariable long roomId, ChatMessageRequestDTO msg, Principal principal){
       
        ChatMessageResponseDTO msg2=   chatService.saveMessage(msg,roomId,principal);
        messagingTemplate.convertAndSend("/topic/room/"+roomId, msg2);
    }

}
