package com.shortly.Services;

import com.shortly.Models.User;
import com.shortly.Models.UserPrinciple;
import com.shortly.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userFound = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));;

        if (userFound == null){
            System.out.println("User not found !");
            throw new UsernameNotFoundException("User missing");
        }
        return new UserPrinciple(userFound);
    }
}
