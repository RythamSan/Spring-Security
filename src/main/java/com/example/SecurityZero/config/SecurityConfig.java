package com.example.SecurityZero.config;


import com.example.SecurityZero.entity.UserModel;
import com.example.SecurityZero.repository.UserRepo;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;

@Configuration
public class SecurityConfig {


    @Bean
    public JwtDecoder jwtDecoder(@Value("${jwt.secret}") String secret){
        SecretKey key= Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(secret)
        );
        return NimbusJwtDecoder.withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration){
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session->session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS
                ))
                .authorizeHttpRequests(
                        auth->

                                auth.
                                        requestMatchers("/api/login").permitAll()
                                        .requestMatchers("/api/users")
                                        .hasRole("USER")
                                        .anyRequest()
                                        .authenticated()
                )
//                .oauth2Client(Customizer.withDefaults())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

//                .oauth2ResourceServer(oauth2 ->
//                        oauth2.jwt(
//                                org.springframework.security.config.Customizer
//                                        .withDefaults()
//                        )
//                )

                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
     public CommandLineRunner createUsers(UserRepo repo){
        return args -> {
            if(repo.findByUsername("Sargam").isEmpty()){
                repo.save(
                        UserModel.builder()
                                .username("Sargam")
                                .password(passwordEncoder().encode("abc123"))
                                .role("USER")
                                .build()
                );
            }
            if(repo.findByUsername("Rytham").isEmpty()){
                repo.save(
                       UserModel.builder()
                               .username("Rytham")
                               .role("ADMIN")
                               .password(passwordEncoder().encode("abc123"))
                               .build()
                );
            }
        };
    }


}
