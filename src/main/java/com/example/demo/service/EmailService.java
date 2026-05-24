package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    public void sendVerificationEmail(String to, String username, String token) {
        String subject = "Verify Your Email - College Rating System";
        String verificationUrl = "http://localhost:3000/verify?token=" + token;
        String message = "Hello " + username + ",\n\n"
                + "Please click the link below to verify your email address:\n"
                + verificationUrl + "\n\n"
                + "This link will expire in 24 hours.\n\n"
                + "Thank you for joining College Rating System!\n\n"
                + "Best regards,\nCollege Rating Team";
        
        sendEmail(to, subject, message);
    }
    
    public void sendPasswordResetEmail(String to, String username, String token) {
        String subject = "Reset Your Password - College Rating System";
        String resetUrl = "http://localhost:3000/reset-password?token=" + token;
        String message = "Hello " + username + ",\n\n"
                + "We received a request to reset your password.\n"
                + "Please click the link below to reset your password:\n"
                + resetUrl + "\n\n"
                + "This link will expire in 1 hour.\n\n"
                + "If you didn't request this, please ignore this email.\n\n"
                + "Best regards,\nCollege Rating Team";
        
        sendEmail(to, subject, message);
    }
    
    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("noreply@collegerating.com");
        
        mailSender.send(message);
    }
}