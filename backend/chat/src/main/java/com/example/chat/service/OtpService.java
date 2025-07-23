package com.example.chat.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.chat.dto.OtpVerificationDTO;
import com.example.chat.model.OtpVerification;
import com.example.chat.repository.OtpVerificationRepository;
import com.example.chat.util.EmailSender;

@Service
public class OtpService {

    private final EmailSender emailSender;
    
    private final OtpVerificationRepository otpVerificationRepository;

    public OtpService(EmailSender emailSender, OtpVerificationRepository otpVerificationRepository) {
        this.emailSender = emailSender;
        this.otpVerificationRepository = otpVerificationRepository;
    }

    public Boolean generateAndSendOtp(String toEmail) {
        String otp = generateOtp(6); 
        String subject = "Your OTP Code";
        String content = "Your OTP code is: " + otp;

        String result = emailSender.sendEmail(toEmail, subject, content, null);
        if (!result.equalsIgnoreCase("SUCCESS")) return false;

        OtpVerification otpVerification = new OtpVerification();
        otpVerification.setEmail(toEmail);
        otpVerification.setOtp(otp);
        otpVerification.setCreatedAt(LocalDateTime.now());
        otpVerification.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otpVerification.setVerified(false);
        otpVerificationRepository.save(otpVerification);
        return true;
    
      
    }

    public boolean verifyOtp(OtpVerificationDTO otpVerifyDto){
        String email=otpVerifyDto.getEmail();
        String otp=otpVerifyDto.getOtp();
       
        Optional<OtpVerification> optionalOtpVerification = otpVerificationRepository.findTopByEmailOrderByCreatedAtDesc(email);
        if (optionalOtpVerification.isEmpty())  return false;
       
        OtpVerification otpVerification = optionalOtpVerification.get();
        
        if(otpVerification.isVerified()) return false;

        otpVerification.setLastAttemptAt(LocalDateTime.now());

        if(otpVerification.getExpiresAt().isBefore(LocalDateTime.now())) return false;
        if(!otpVerification.getOtp().equals(otp)){ 
            otpVerification.setAttempts(otpVerification.getAttempts() + 1);
            otpVerificationRepository.save(otpVerification);
            return false;
        }
        otpVerification.setVerified(true);
        otpVerificationRepository.save(otpVerification);
        return true;

    }

    private String generateOtp(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}
