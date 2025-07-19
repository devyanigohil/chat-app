package com.example.chat.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FriendRequest {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    private boolean accepted ;

    private boolean rejected ;
    
    private boolean handled ;

    private LocalDateTime requestedAt;

    private LocalDateTime respondedAt;

    private String message; 
}