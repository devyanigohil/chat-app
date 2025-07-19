package com.example.chat.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.chat.dto.ChatMessageResponseDTO;
import com.example.chat.dto.ChatRoomDTO;
import com.example.chat.repository.UserRepository;
import com.example.chat.service.ChatRoomService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/rooms")
public class ChatRoomController {

    private ChatRoomService chatRoomService;
    private final UserRepository userRepository;

    public ChatRoomController(ChatRoomService chatRoomService,UserRepository userRepository) {
        this.chatRoomService = chatRoomService;
        this.userRepository=userRepository;
    }


    @PostMapping
    public ResponseEntity<ChatRoomDTO> createRoom(@RequestBody ChatRoomDTO chatRoomDTO, Principal principal) {
        ChatRoomDTO room = chatRoomService.createRoom(principal,chatRoomDTO);
        return ResponseEntity.ok(room);
    }

    @GetMapping
    public ResponseEntity<List<ChatRoomDTO>> getChatRooms(Principal principal) {
        List<ChatRoomDTO> rooms=chatRoomService.getByCreatedBy(principal);
        return ResponseEntity.ok().body(rooms);
    }

    @GetMapping("/{roomid}/messages")
    public List<ChatMessageResponseDTO> getMessages(@PathVariable String roomid) {
        return chatRoomService.getMessages(roomid);
    }
    
    
}
