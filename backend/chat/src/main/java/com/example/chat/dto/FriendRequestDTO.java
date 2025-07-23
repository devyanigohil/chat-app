package com.example.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendRequestDTO {

    private Long id;

    private String receiver;
    
    private String sender;
    
    private String msg;
}
