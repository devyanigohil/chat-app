package com.example.chat.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.chat.dto.FriendRequestActionDTO;
import com.example.chat.dto.FriendRequestDTO;
import com.example.chat.service.FriendRequestService;

@RestController
@RequestMapping("/api/friend-requests")
public class FriendRequestController {
    private final FriendRequestService friendRequestService;
    public FriendRequestController(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    @PostMapping
    public ResponseEntity<String> sendRequest(@RequestBody FriendRequestDTO request, Principal principal) {
        friendRequestService.sendRequest(principal.getName(), request);
        return ResponseEntity.ok("Friend request sent to " + request.getReceiver());
    }

    @PutMapping
    public void handleRequest(@RequestBody FriendRequestActionDTO requestActionDTO, Principal principal) {
        friendRequestService.handleRequest(requestActionDTO, principal);
    }

    @GetMapping
    public List<FriendRequestDTO> getPendingRequests(Principal principal) {
        return friendRequestService.getPendingRequests(principal.getName());
    }
}