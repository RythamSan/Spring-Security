package com.example.SecurityZero.service;

import com.example.SecurityZero.entity.UserModel;
import com.example.SecurityZero.repository.UserRepo;
import org.springframework.boot.security.autoconfigure.SecurityProperties;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepo repo;

    public CustomUserDetailsService(UserRepo repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel users=repo.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("Not found")) ;

        return User
                .withUsername(users.getUsername())
                .password(users.getPassword())
                .roles(users.getRole())
                .build();
    }
}
