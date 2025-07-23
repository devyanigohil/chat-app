package com.example.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpVerificationDTO {
    
    private String email;
    private String otp;

}