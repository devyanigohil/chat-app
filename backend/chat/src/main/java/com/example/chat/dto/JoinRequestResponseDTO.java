package com.example.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRequestResponseDTO {

    private long id;
    private String msg;
    private String roomname;
    private String admin;
    private String roomDescription;

}
