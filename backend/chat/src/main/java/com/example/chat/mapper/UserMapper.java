package com.example.chat.mapper;


import com.example.chat.dto.UserDTO;
import com.example.chat.model.User;

public class UserMapper {

    public static User toModel(UserDTO userdto){
        User user=new User();
        user.setUsername(userdto.getUsername());
        user.setPassword(userdto.getPassword());
        user.setEmail(userdto.getEmail());
        return user;
    }

    public static UserDTO toDto(User user){
        UserDTO userDTO=new UserDTO();
        userDTO.setUsername(user.getUsername());

        return userDTO;
    }

}
