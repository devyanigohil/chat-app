package com.example.chat.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.chat.dto.JwtResponseDTO;
import com.example.chat.dto.LoginRequestDTO;
import com.example.chat.dto.UserDTO;
import com.example.chat.service.UserService;
import com.example.chat.util.JwtUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;
   
    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserDTO> signup(@RequestBody UserDTO userDTO) { 
        return ResponseEntity.ok().body(userService.createUser(userDTO));
    }

    @PostMapping("/loginuser")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            String token = jwtUtil.generateToken(loginRequest.getUsername());
            return ResponseEntity.ok().body(new JwtResponseDTO(token));
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid credentials");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String query, Principal principal) {
        List<UserDTO> users=userService.searchUsers(query, principal.getName());
        return ResponseEntity.ok().body(users);
    }
    
    
}
