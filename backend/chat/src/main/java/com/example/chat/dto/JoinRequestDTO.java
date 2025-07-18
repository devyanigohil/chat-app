package com.example.chat.dto;

import lombok.Setter;

import lombok.Getter;

@Getter
@Setter
public class JoinRequestDTO {
    private Long roomId;              // The chat room ID
    private String targetUsername;    // The user to invite or the user requesting
    private boolean sentByAdmin;        // "REQUEST" or "INVITE" (optional, based on who initiates)
    private String message;           // Optional: request message
}
