package com.example.chat.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class JoinRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ChatRoom chatRoom;
    @ManyToOne
    private User requester; // who initiated the request (either admin or user)

    @ManyToOne
    private User recipient; // who has to take action (admin if requester is user, or user if requester is admin)

    private boolean approved = false;
    private boolean rejected = false;
    private boolean handled = false;

    private String message; // optional: "Hi, please add me" or "You’ve been invited to..."

    private boolean sentByAdmin; // to distinguish user request vs admin invitation

    private LocalDateTime requestedAt;

    private LocalDateTime respondedAt;

    @ManyToOne
    private User handledBy; // who approved/rejected the request

}

