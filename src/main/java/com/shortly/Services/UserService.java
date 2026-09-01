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

    public UserService(UserRepo repo, AuthenticationManager authManager, JwtService service){
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

    public String registerUser(UserDataInput userData) {
        User newUser = new User();

        newUser.setUsername(userData.username());
        newUser.setPassword(encoder.encode(userData.password()));
        newUser.setEmail(userData.email());

        newUser = repo.save(newUser);
        String jwtToken = jwtService.generateToken(userData.username());

        return jwtToken;
    }

    public String logOutUser() {
        return "Logged out";
    }

    public UserProfileWithData getUserProfile(String username) {
         return repo.getUserProfileWithData(username);
//        User user = repo.findByUsername(username);
//        user.setPassword(null);

//        repo.getUrlData(user.getUserId());

//        return user;
    }

    public User updateUser(String username, UpdateUserRequest request) {
        User user = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // ignored for now...

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

        return repo.save(user);
    }

    public String deleteUser(String username) {
        User user = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        repo.delete(user);
        return "User deleted successfully";
    }
}
