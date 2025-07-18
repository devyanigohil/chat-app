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
import com.example.chat.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO createUser(UserDTO userDTO){
        User user=UserMapper.toModel(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRegisterDate(LocalDateTime.now());
        userRepository.save(user);
        return UserMapper.toDto(user);
    }
    
    public List<UserDTO> searchUsers(String query, String principal){
        List<User> users=userRepository.getByUsername(query,principal);
        return users.stream().
                        map(UserMapper :: toDto)
                        .toList();
        
    }
  
}
