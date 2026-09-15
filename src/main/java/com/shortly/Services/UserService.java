package com.shortly.Services;

import com.shortly.DTO.userDTOs.UpdateUserRequest;
import com.shortly.DTO.userDTOs.UserDataInput;
import com.shortly.DTO.userDTOs.UserProfileWithData;
import com.shortly.Exceptions.BadRequestException;
import com.shortly.Models.ResetToken;
import com.shortly.Models.User;
import com.shortly.Repository.ResetTokenRepo;
import com.shortly.Repository.UserRepo;
import com.shortly.Utils.CodeGenerator;
import com.shortly.Utils.EmailEngine;
import com.shortly.Utils.ErrorCodes;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    private final UserRepo repo;
    private final ResetTokenRepo tokenRepo;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final EmailEngine mailSender;

    public UserService(UserRepo repo, AuthenticationManager authManager, JwtService service, EmailEngine mail, ResetTokenRepo tokenRepo){
        this.jwtService = service;
        this.authManager = authManager;
        this.repo = repo;
        this.tokenRepo = tokenRepo;
        this.mailSender = mail;
    }

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String userLogin(UserDataInput userData) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(userData.username(), userData.password())
        );

        return jwtService.generateToken(userData.username());
    }

    public String registerUser(UserDataInput request) {
        // Check existing username and email
        List<User> ifExist = repo.findByUsernameOrEmail(request.username(), request.email());

        if (!ifExist.isEmpty()){
            boolean isUsername = ifExist.get(0).getUsername().equals(request.username());
            throw new BadRequestException(isUsername ? ErrorCodes.USERNAME_EXISTS : ErrorCodes.EMAIL_EXISTS);
        }

        User newUser = new User();

        newUser.setUsername(request.username());
        newUser.setPassword(encoder.encode(request.password()));
        newUser.setEmail(request.email());

        repo.save(newUser);
        return jwtService.generateToken(request.username());
    }

    public UserProfileWithData getUserProfile(String username) {
        return repo.getUserProfileWithData(username);
    }

    public User updateUser(String username, UpdateUserRequest request) {
        User user = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<User> ifExist = repo.findByUsernameOrEmail(request.username(), request.email());

        if (!ifExist.isEmpty()){
            boolean isUsername = ifExist.get(0).getUsername().equals(request.username());
            throw new BadRequestException(isUsername ? ErrorCodes.USERNAME_EXISTS : ErrorCodes.EMAIL_EXISTS);
        }

        if(request.isUsername()){
            user.setUsername(request.username());
        } else {
            user.setEmail(request.email());
        }

        return repo.save(user);
    }

    public void deleteUser(String username) {
        User user = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        repo.delete(user);
    }

    public void initiatePasswordReset(String email) {
        User user = repo.findByEmail(email).orElse(null);

        if (user == null) {
            // Return early or throw generic success to avoid email enumeration attacks
             return ;
        }

        String rawCode = CodeGenerator.generate6DigitCode();
        String hashedCode = encoder.encode(rawCode); // Hashing code before saving

        ResetToken token = new ResetToken();
        token.setUser(user);
        token.setTokenHash(hashedCode);
        token.setExpiresAt(LocalDateTime.now().plusMinutes(10)); // Valid for 10 minutes

        tokenRepo.save(token);
        mailSender.sendEmail(rawCode, email);
    }

    @Transactional
    public void resetPassword(
            String email,
            String code,
            String newPassword) {

        User user = repo.findByEmail(email)
                .orElseThrow(() ->
                        new BadRequestException("Invalid reset request"));

        ResetToken token =
                tokenRepo.findByUser(user)
                        .orElseThrow(() ->
                                new BadRequestException("Invalid reset request"));

        // A used token can never be reused.
        if (token.isUsed()) {
            throw new BadRequestException("Invalid reset request");
        }

        // Prevent brute-force attempts against the OTP.
        if (token.getAttempts() >= 5) {
            throw new BadRequestException("Too many attempts");
        }

        // OTP expires after 10 minutes.
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Reset code expired");
        }

        // Hash the user-provided code and compare it with
        // the hash stored in the database.
        if(!encoder.matches(code, token.getTokenHash())){
            // Increment failed attempts before rejecting the request.
            token.setAttempts(token.getAttempts() + 1);
            tokenRepo.save(token);

            throw new BadRequestException("Invalid reset code");
        }

        // Always hash passwords using Spring's PasswordEncoder.
        user.setPassword(encoder.encode(newPassword));
        repo.save(user);

        // Make the reset token single-use.
        token.setUsed(true);
        tokenRepo.save(token);
    }
}