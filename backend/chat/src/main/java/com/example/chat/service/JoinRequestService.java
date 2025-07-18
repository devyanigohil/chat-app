package com.example.chat.service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.example.chat.dto.JoinRequestActionDTO;
import com.example.chat.dto.JoinRequestDTO;
import com.example.chat.dto.JoinRequestResponseDTO;
import com.example.chat.mapper.ChatRoomMapper;
import com.example.chat.mapper.JoinRequestMapper;
import com.example.chat.model.ChatRoom;
import com.example.chat.model.JoinRequest;
import com.example.chat.model.User;
import com.example.chat.repository.ChatRoomRepository;
import com.example.chat.repository.JoinRequestRepository;
import com.example.chat.repository.UserRepository;

@Service
public class JoinRequestService {


    ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final JoinRequestRepository joinRequestRepository;
    private final UserRepository userRepo;

    public JoinRequestService(ChatRoomRepository chatRoomRepository,UserRepository userRepository
                            ,JoinRequestRepository joinRequestRepository,UserRepository userRepo) {
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository=userRepository;
        this.joinRequestRepository=joinRequestRepository;
        this.userRepo=userRepo;
    }


    public void createJoinRequest(JoinRequestDTO joinRequestDTO, Principal principal){
       JoinRequest joinRequest= JoinRequestMapper.JoinRequestDTOtoModel(joinRequestDTO, chatRoomRepository);
       User recipient;
       if(joinRequest.isSentByAdmin()){
        if(!joinRequest.getChatRoom().getCreatedby().equals(userRepository.findByUsername(principal.getName()))){
                throw new AccessDeniedException("Only admin can invite.");
        }
          recipient = userRepo.findByUsername(joinRequestDTO.getTargetUsername());
           
    }else{
                recipient=joinRequest.getChatRoom().getAdmin();
        }
        joinRequest.setHandled(false);
        joinRequest.setRecipient(recipient);
        joinRequest.setRequester(userRepository.findByUsername(principal.getName()));
        joinRequest.setRequestedAt(LocalDateTime.now());
        joinRequestRepository.save(joinRequest);
        
    }

    public void handleJoinRequest(JoinRequestActionDTO joinRequestActionDTO,Principal principal){
         JoinRequest request = joinRequestRepository.findById(joinRequestActionDTO.getRequestId())
                        .orElseThrow(() -> new RuntimeException("Request not found"));
        
        if(!request.getRecipient().equals(userRepository.findByUsername(principal.getName()))){
             throw new AccessDeniedException("Access Denied");
        }
        request.setApproved(joinRequestActionDTO.isApprove());
        request.setRejected(!joinRequestActionDTO.isApprove());
        request.setRespondedAt(LocalDateTime.now());
        request.setHandledBy(userRepository.findByUsername(principal.getName()));
        if(joinRequestActionDTO.isApprove()){
            ChatRoom room=request.getChatRoom();
            User participant=request.isSentByAdmin()?request.getRecipient():request.getRequester();
            room.getParticipants().add(participant);
            chatRoomRepository.save(room);
        }
        request.setHandled(true);
        joinRequestRepository.save(request);
    }


    public  List<JoinRequestResponseDTO> getRequestsForUser(Principal principal){
        User user=userRepository.findByUsername(principal.getName());
        List<JoinRequest> requests=joinRequestRepository.findPendingRequests(principal.getName());
     //   System.out.println("request--->"+requests);
        return requests.stream()
                        .map(request  -> JoinRequestMapper.joinReqResponseModeltoDTO(request, chatRoomRepository) )
                        .collect(Collectors.toList());
    }
}
