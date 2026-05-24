package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    private String role = "USER";
    
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Admin features fields
    @Column(nullable = false)
    private boolean banned = false;
    
    private LocalDateTime bannedUntil = null;
    
    private String profilePicture = null;
    
    private LocalDateTime lastLogin = null;
    
    // Email verification fields
    private boolean verified = false;
    
    private String verificationToken = null;
    
    private LocalDateTime verificationTokenExpiry = null;
    
    // Password reset fields
    private String resetToken = null;
    
    private LocalDateTime resetTokenExpiry = null;
    
    // Constructors
    public User() {}
    
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    
    // Getters and Setters
    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }
    
    public String getUsername() { 
        return username; 
    }
    
    public void setUsername(String username) { 
        this.username = username; 
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }
    
    public String getPassword() { 
        return password; 
    }
    
    public void setPassword(String password) { 
        this.password = password; 
    }
    
    public String getRole() { 
        return role; 
    }
    
    public void setRole(String role) { 
        this.role = role; 
    }
    
    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }
    
    // Admin features getters and setters
    public boolean isBanned() { 
        return banned; 
    }
    
    public void setBanned(boolean banned) { 
        this.banned = banned; 
    }
    
    public LocalDateTime getBannedUntil() { 
        return bannedUntil; 
    }
    
    public void setBannedUntil(LocalDateTime bannedUntil) { 
        this.bannedUntil = bannedUntil; 
    }
    
    public String getProfilePicture() { 
        return profilePicture; 
    }
    
    public void setProfilePicture(String profilePicture) { 
        this.profilePicture = profilePicture; 
    }
    
    public LocalDateTime getLastLogin() { 
        return lastLogin; 
    }
    
    public void setLastLogin(LocalDateTime lastLogin) { 
        this.lastLogin = lastLogin; 
    }
    
    // Email verification getters and setters
    public boolean isVerified() { 
        return verified; 
    }
    
    public void setVerified(boolean verified) { 
        this.verified = verified; 
    }
    
    public String getVerificationToken() { 
        return verificationToken; 
    }
    
    public void setVerificationToken(String verificationToken) { 
        this.verificationToken = verificationToken; 
    }
    
    public LocalDateTime getVerificationTokenExpiry() { 
        return verificationTokenExpiry; 
    }
    
    public void setVerificationTokenExpiry(LocalDateTime verificationTokenExpiry) { 
        this.verificationTokenExpiry = verificationTokenExpiry; 
    }
    
    // Password reset getters and setters
    public String getResetToken() { 
        return resetToken; 
    }
    
    public void setResetToken(String resetToken) { 
        this.resetToken = resetToken; 
    }
    
    public LocalDateTime getResetTokenExpiry() { 
        return resetTokenExpiry; 
    }
    
    public void setResetTokenExpiry(LocalDateTime resetTokenExpiry) { 
        this.resetTokenExpiry = resetTokenExpiry; 
    }
}