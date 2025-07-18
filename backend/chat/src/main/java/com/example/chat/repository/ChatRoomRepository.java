package com.example.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.chat.model.ChatRoom;
import com.example.chat.model.User;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long>{

    ChatRoom findByName(String roomname);

    Optional<ChatRoom> findById(Long id);

    List<ChatRoom> findByCreatedby(User user);

}
