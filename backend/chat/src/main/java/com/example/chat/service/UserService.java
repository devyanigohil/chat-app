package com.example.chat.service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.chat.dto.UserDTO;
import com.example.chat.mapper.UserMapper;
import com.example.chat.model.User;
import com.example.chat.repository.FriendRequestRepository;
import com.example.chat.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FriendRequestRepository friendRequestRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,FriendRequestRepository friendRequestRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.friendRequestRepository = friendRequestRepository;
    }

    public UserDTO createUser(UserDTO userDTO){
        User user=UserMapper.toModel(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRegisterDate(LocalDateTime.now());
        userRepository.save(user);
        return UserMapper.toDto(user);
    }
    
    public List<UserDTO> searchUsers(String query, String principal,Long chatRoomId) {
        List<User> users=userRepository.searchForChatRoomInvite(query,principal,chatRoomId); // Assuming 1L is a placeholder for chatRoomId
        return users.stream().
                        map(UserMapper :: toDto)
                        .toList();
        
    }

        
    public List<UserDTO> searchUsersforFriendRequest(String query, String principal){
        User user=userRepository.findByUsername(principal);
        List<User> users=friendRequestRepository.searchForFriendRequest(query,user.getUsername(),user.getId()); // Assuming 1L is a placeholder for chatRoomId
        return users.stream().
                        map(UserMapper :: toDto)
                        .toList();
        
    }
    
  
}
