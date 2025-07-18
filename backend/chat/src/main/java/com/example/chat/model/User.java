package com.example.chat.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users") 
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String username;

    private String password;

    private String email;

    private LocalDateTime registerDate;

     @OneToMany(mappedBy = "createdby", cascade = CascadeType.ALL)
    private List<ChatRoom> createdRooms = new ArrayList<>();

    @ManyToMany(mappedBy = "participants")
    private Set<ChatRoom> chatRooms = new HashSet<>();

    @OneToMany(mappedBy = "admin" ,cascade=CascadeType.ALL)
    private List<ChatRoom> roomsCreated;

    @OneToMany(mappedBy = "sender",cascade = CascadeType.ALL)
    private List<ChatMessage> messages;
}
