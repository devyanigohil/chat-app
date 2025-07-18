package com.example.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageResponseDTO {

      private String sender;
    private String content;
    private String room;

}
