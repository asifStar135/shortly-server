package com.shortly.Services;

import com.shortly.DTO.UpdateUserRequest;
import com.shortly.DTO.UserDataInput;
import com.shortly.DTO.UserProfileWithData;
import com.shortly.Models.User;
import com.shortly.Repository.UserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepo repo;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

<<<<<<< Updated upstream
    public UserService(UserRepo repo, AuthenticationManager authManager, JwtService service){
=======
    public UserService(UserRepo repo, AuthenticationManager authManager, JwtService service, EmailEngine mail, ResetTokenRepo tokenRepo) {
>>>>>>> Stashed changes
        this.jwtService = service;
        this.authManager = authManager;
        this.repo = repo;
    }

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String userLogin(UserDataInput userData) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(userData.username(), userData.password())
        );

        if(auth.isAuthenticated()){
            return jwtService.generateToken(userData.username());
        }
        return "User authentication failed !";
    }

<<<<<<< Updated upstream
    public String registerUser(UserDataInput userData) {
=======
    public String registerUser(UserDataInput request) {
        // Check existing username and email
        List<User> ifExist = repo.findByUsernameOrEmail(request.username(), request.email());

        if (!ifExist.isEmpty()) {
            boolean isUsername = ifExist.get(0).getUsername().equals(request.username());
            throw new BadRequestException(isUsername ? ErrorCodes.USERNAME_EXISTS : ErrorCodes.EMAIL_EXISTS);
        }

>>>>>>> Stashed changes
        User newUser = new User();

        newUser.setUsername(userData.username());
        newUser.setPassword(encoder.encode(userData.password()));
        newUser.setEmail(userData.email());

        repo.save(newUser);
        String jwtToken = jwtService.generateToken(userData.username());

        return jwtToken;
    }

    public UserProfileWithData getUserProfile(String username) {
        return repo.getUserProfileWithData(username);
    }

    public User updateUser(String username, UpdateUserRequest request) {
        User user = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // ignored for now...

<<<<<<< Updated upstream
        // Check if new username is already taken
//        if (!user.getUsername().equals(request.getUsername())
//                && userRepository.existsByUsername(request.getUsername())) {
//            throw new RuntimeException("Username already exists");
//        }
//          Check if new email is already taken
//        if (!user.getEmail().equals(request.getEmail())
//                && userRepository.existsByEmail(request.getEmail())) {
//            throw new RuntimeException("Email already exists");
//        }

        user.setUsername(request.username());
        user.setEmail(request.email());
=======
        if (!ifExist.isEmpty()) {
            boolean isUsername = ifExist.get(0).getUsername().equals(request.username());
            throw new BadRequestException(isUsername ? ErrorCodes.USERNAME_EXISTS : ErrorCodes.EMAIL_EXISTS);
        }

        if (request.isUsername()) {
            user.setUsername(request.username());
        } else {
            user.setEmail(request.email());
        }
>>>>>>> Stashed changes

        return repo.save(user);
    }

    public String deleteUser(String username) {
        User user = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        repo.delete(user);
<<<<<<< Updated upstream
        return "User deleted successfully";
=======
    }

    public void initiatePasswordReset(String email) {
        User user = repo.findByEmail(email).orElse(null);

        if (user == null) {
            // Return early or throw generic success to avoid email enumeration attacks
            return;
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
                        new BadRequestException(ErrorCodes.INVALID_CODE));

        List<ResetToken> tokens =
                tokenRepo.findByUserAndUsed(user, false);

        if (tokens.size() == 0) {
            throw new BadRequestException(ErrorCodes.INVALID_CODE);
        }
        ResetToken token = tokens.get(0);

        // Prevent brute-force attempts against the OTP.
        if (token.getAttempts() >= 5) {
            throw new BadRequestException(ErrorCodes.TOO_MANY_ATTEMPTS);
        }

        // OTP expires after 10 minutes.
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException(ErrorCodes.INVALID_CODE);
        }

        // Hash the user-provided code and compare it with
        // the hash stored in the database.
        if (!encoder.matches(code, token.getTokenHash())) {
            // Increment failed attempts before rejecting the request.
            token.setAttempts(token.getAttempts() + 1);
            tokenRepo.save(token);

            throw new BadRequestException(ErrorCodes.INVALID_CREDENTIALS);
        }

        // Always hash passwords using Spring's PasswordEncoder.
        user.setPassword(encoder.encode(newPassword));
        repo.save(user);

        // Make the reset token single-use.
        token.setUsed(true);
        tokenRepo.save(token);
>>>>>>> Stashed changes
    }
}