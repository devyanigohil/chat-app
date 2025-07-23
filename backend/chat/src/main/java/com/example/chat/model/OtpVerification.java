package com.example.chat.model;

import java.time.LocalDateTime;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OtpVerification {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    private String email;

    private String otp;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private boolean Verified;

    private int attempts = 0;

    private LocalDateTime lastAttemptAt;


}
