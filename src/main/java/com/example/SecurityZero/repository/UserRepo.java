package com.example.SecurityZero.repository;

import com.example.SecurityZero.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepo extends JpaRepository<UserModel,Integer> {
    Optional<UserModel> findByUsername(String name);
}
