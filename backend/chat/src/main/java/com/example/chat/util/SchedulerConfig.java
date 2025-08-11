package com.example.chat.util;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;

import com.example.chat.model.FriendRequest;
import com.example.chat.model.JoinRequest;
import com.example.chat.repository.FriendRequestRepository;
import com.example.chat.repository.JoinRequestRepository;

public class SchedulerConfig {

    private final FriendRequestRepository friendRequestRepo;
    private final JoinRequestRepository joinRequestRepo;

    public SchedulerConfig(FriendRequestRepository friendRequestRepo, JoinRequestRepository joinRequestRepo) {
        this.friendRequestRepo = friendRequestRepo;
        this.joinRequestRepo = joinRequestRepo;
    }

    //if user has not responded to friend request or join request in 3 months, delete the request
    @Scheduled(cron="0 0 0 * * *") // Runs every day at midnight
    public void deleteExpiredRequests(){
        List<FriendRequest> expiredFriendRequests = friendRequestRepo.findExpiredRequests();
        if (!expiredFriendRequests.isEmpty()) {
            friendRequestRepo.deleteAll(expiredFriendRequests);
        }   
        List<JoinRequest> expiredJoinRequests = joinRequestRepo.findExpiredRequests();
        if (!expiredJoinRequests.isEmpty()) {
            joinRequestRepo.deleteAll(expiredJoinRequests);
        }
    }

}
