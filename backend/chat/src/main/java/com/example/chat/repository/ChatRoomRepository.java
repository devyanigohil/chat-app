package com.example.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.chat.model.ChatRoom;
import com.example.chat.model.User;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long>{

    ChatRoom findByName(String roomname);

    Optional<ChatRoom> findById(Long id);

    List<ChatRoom> findByCreatedby(User user);

    List<ChatRoom> findByParticipantsContaining(User user);

   @Query("SELECT r FROM ChatRoom r " +
       "WHERE r.type = 'PRIVATE' " +
       "AND SIZE(r.participants) = 2 " +
       "AND EXISTS (SELECT p FROM r.participants p WHERE p.id = :user1Id) " +
       "AND EXISTS (SELECT p FROM r.participants p WHERE p.id = :user2Id)")
ChatRoom findPrivateRoomByParticipants(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);
}
