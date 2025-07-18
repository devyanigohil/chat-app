package com.example.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponseDTO {

    
    public JwtResponseDTO(String token) {
        this.token = token;
    }

    private String token;
}
