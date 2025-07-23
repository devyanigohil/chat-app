package com.example.chat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.chat.model.OtpVerification;

@Repository
public interface OtpVerificationRepository extends  JpaRepository<OtpVerification, Long> {

        Optional<OtpVerification> findTopByEmailOrderByCreatedAtDesc(String email);


}
