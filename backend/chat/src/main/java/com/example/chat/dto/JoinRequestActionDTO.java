package com.example.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRequestActionDTO {
    private Long requestId;
    private boolean approve; // "ACCEPT" or "REJECT"
}

