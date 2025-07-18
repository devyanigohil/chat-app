package com.example.chat.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.chat.dto.JoinRequestActionDTO;
import com.example.chat.dto.JoinRequestDTO;
import com.example.chat.dto.JoinRequestResponseDTO;
import com.example.chat.service.JoinRequestService;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/join-requests")
public class JoinRequestController {

        private final JoinRequestService joinRequestService;

        public JoinRequestController(JoinRequestService joinRequestService) {
            this.joinRequestService = joinRequestService;
        }
        

        @PostMapping
        public ResponseEntity<String> createJoinRequest(@RequestBody List<JoinRequestDTO> joinRequestDTOlist,Principal principal) {
            for( JoinRequestDTO joinRequestDTO : joinRequestDTOlist){
                joinRequestService.createJoinRequest(joinRequestDTO,principal);
            }
            return ResponseEntity.ok("Request to Join ChatRoom Sent!");
        }

        @PutMapping
        public ResponseEntity<String> handleJoinRequest( @RequestBody JoinRequestActionDTO joinRequestActionDTO, Principal principal) {
            joinRequestService.handleJoinRequest(joinRequestActionDTO, principal);
            return ResponseEntity.ok("done");
        }

        @GetMapping
        public ResponseEntity<List<JoinRequestResponseDTO>> getMyJoinRequests(Principal principal) {
            List<JoinRequestResponseDTO> requests = joinRequestService.getRequestsForUser(principal);
            return ResponseEntity.ok(requests);
        }
        

}
