package com.example.SecurityZero.controller;

import com.example.SecurityZero.dto.LoginRequest;
import com.example.SecurityZero.dto.LoginResponse;
import com.example.SecurityZero.entity.UserModel;
import com.example.SecurityZero.repository.UserRepo;
import com.example.SecurityZero.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SpringController {


    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepo repo;

    public SpringController(AuthenticationManager authenticationManager,JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService=jwtService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest){
       Authentication authentication=authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(
                       loginRequest.username(),
                       loginRequest.password()
               )
       );
        UserDetails user=(UserDetails) authentication.getPrincipal();
        assert user != null;
        String token=jwtService.generateToken(user);

       return new LoginResponse(token,"Bearer");
   }


   @GetMapping("/names")
    public List<String> names(){
        List<UserModel> list=repo.findAll();
        List<String> names=new ArrayList<>();
        for(UserModel userModel: list){
            names.add(userModel.getUsername());
        }
        return names;
   }


}
