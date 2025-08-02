package com.example.chat.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageResponseDTO {

    private String sender;
    private String content;
    private String room;
    private LocalDateTime timestamp;
}
