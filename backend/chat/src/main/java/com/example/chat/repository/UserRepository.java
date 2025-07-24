package com.example.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.chat.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    @Query(value = "SELECT * FROM users WHERE username LIKE CONCAT('%', ?1, '%') and username <> ?2" ,nativeQuery = true)
    List<User> getByUsername(String query,String username);

    @Query(value = "SELECT * FROM users u " +
        "WHERE u.username LIKE CONCAT('%', ?1, '%') " +
        "AND u.username <> ?2 " +
        "AND u.id NOT IN (" +
        "  SELECT user_id FROM chatroom_users WHERE chatroom_id = ?3" +
        ") " +
        "AND u.id NOT IN (" +
        "  SELECT recipient_id FROM join_request WHERE chat_room_id = ?3 AND handled = false" +
        ")", nativeQuery = true)
List<User> searchForChatRoomInvite(String query, String currentUsername, Long chatRoomId);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    User findByEmail(String email);

}
