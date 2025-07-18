package com.example.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomDTO {

    private long id;

    private String name;

    private String description;

    private boolean isAdmin;


}
