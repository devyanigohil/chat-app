package com.example.chat.mapper;

import com.example.chat.dto.JoinRequestDTO;
import com.example.chat.dto.JoinRequestResponseDTO;
import com.example.chat.model.JoinRequest;
import com.example.chat.repository.ChatRoomRepository;

public class JoinRequestMapper {

    public static JoinRequest JoinRequestDTOtoModel(JoinRequestDTO joinRequestDTO,ChatRoomRepository chatRoomRepository){
        JoinRequest joinRequest=new JoinRequest();
        joinRequest.setMessage(joinRequestDTO.getMessage());
        joinRequest.setSentByAdmin(joinRequestDTO.isSentByAdmin());
        joinRequest.setChatRoom(chatRoomRepository.findById( joinRequestDTO.getRoomId()).orElseThrow(() -> new RuntimeException("Chat Room not Found")));
        return joinRequest;
       
    }


    public static JoinRequestDTO JoinRequestModeltoDTO(JoinRequest joinRequest,ChatRoomRepository chatRoomRepository){
        JoinRequestDTO Request=new JoinRequestDTO();
        Request.setMessage(joinRequest.getMessage());
        Request.setSentByAdmin(joinRequest.isSentByAdmin());
        Request.setRoomId(joinRequest.getChatRoom().getId());
        Request.setTargetUsername(joinRequest.getRecipient().getUsername());
        return Request;
       
    }

    public static JoinRequestResponseDTO joinReqResponseModeltoDTO(JoinRequest request, ChatRoomRepository chatRoomRepo){
        JoinRequestResponseDTO requestResponseDTO=new JoinRequestResponseDTO();
        requestResponseDTO.setId(request.getId());
        requestResponseDTO.setAdmin(request.getChatRoom().getCreatedby().getUsername());
        requestResponseDTO.setMsg(request.getMessage());
        requestResponseDTO.setRoomname(request.getChatRoom().getName());
        requestResponseDTO.setRoomDescription(request.getChatRoom().getDescription());

        return requestResponseDTO;
    }
}
