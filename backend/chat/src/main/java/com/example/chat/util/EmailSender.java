package com.example.chat.util;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
public class EmailSender {

    @Autowired
    JavaMailSenderImpl emailSender=new JavaMailSenderImpl();

   

    String host;
    String port;
    String username;
    String password;
    String proxyhost;
    String proxyport;
    Boolean proxyset;
    
    public String sendEmail(String to,String subject,String content, File attachment){
        try{
            emailSender.setHost(host);
            emailSender.setPort(Integer.parseInt(port)); 
            emailSender.setUsername(username);
            emailSender.setPassword(password);
            emailSender.getJavaMailProperties().put("mail.smtp.auth", "true");
            emailSender.getJavaMailProperties().put("mail.smtp.ssl.enable", "true");
            emailSender.getJavaMailProperties().put("mail.smtp.ssl.checkserveridentity", "true");
            emailSender.getJavaMailProperties().put("mail.smtp.ssl.protocols", "TLSv1.2");
            emailSender.getJavaMailProperties().put("mail.smtp.ssl.trust", host);
            emailSender.getJavaMailProperties().put("mail.smtp.from", username);
            if(proxyset){
                emailSender.getJavaMailProperties().put("socksProxyHost", proxyhost);
                emailSender.getJavaMailProperties().put("socksProxyHost", proxyport);
            }
            MimeMessage message= emailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(message);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            if(attachment != null && attachment.exists()) {
                helper.addAttachment(attachment.getName(), attachment);
            }
            emailSender.send(message);
        }catch (Exception e) {
            return "Error in sending email: " + e.getMessage();
        }
         return "SUCCESS" ;

    }
}
