package com.example.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.chat.model.JoinRequest;

@Repository
public interface JoinRequestRepository extends JpaRepository<JoinRequest,Long> {


    @Query(value = "Select * from join_request where recipient_id=(select id from users where username=?1) and handled =false", nativeQuery = true)
    List<JoinRequest> findPendingRequests(String username);

    @Query(value="select * from join_request where handled=false and requested_at<(NOW() - INTERVAL '3 months') ",nativeQuery=true)
    List<JoinRequest> findExpiredRequests();
}
