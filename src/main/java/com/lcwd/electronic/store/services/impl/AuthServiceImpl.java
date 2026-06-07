package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.AuthService;
import com.lcwd.electronic.store.services.EmailService;
import com.lcwd.electronic.store.util.OtpUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void sendResetOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with this email " + email));

        String otp = OtpUtil.generateOtp();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(15);

        user.setResetOtp(otp);
        user.setResetOtpExpiredAt(expiryTime);
        userRepository.save(user);

        try {
            emailService.sendResetOtp(user.getEmail(), otp);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to send OTP");
        }
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with this email " + email));

        if (user.getResetOtp() == null || !user.getResetOtp().equals(otp)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP");
        }

        if (user.getResetOtpExpiredAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetOtp(null);
        user.setResetOtpExpiredAt(null);

        userRepository.save(user);
    }

    @Override
    public void sendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with this email " + email));

        if (Boolean.TRUE.equals(user.getIsAccountVerifiedAt())) {
            return;
        }

        String otp = OtpUtil.generateOtp();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(15);

        user.setVerifyOtp(otp);
        user.setVerifyOtpExpiredAt(expiryTime);
        userRepository.save(user);

        try {
            emailService.sendOtp(user.getEmail(), otp);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to send OTP");
        }
    }

    @Override
    public void verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with this email " + email));

        if (user.getVerifyOtp() == null || !user.getVerifyOtp().equals(otp)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP");
        }

        if (user.getVerifyOtpExpiredAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP expired");
        }

        user.setIsAccountVerifiedAt(true);
        user.setVerifyOtp(null);
        user.setVerifyOtpExpiredAt(null);

        userRepository.save(user);
    }
}
