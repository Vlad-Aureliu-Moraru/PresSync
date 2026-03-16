package com.example.pressync.User.QueryHandlers;

import com.example.pressync.Query;
import com.example.pressync.User.Model.DTOs.UserGetAllDTO;
import com.example.pressync.User.Model.User;
import com.example.pressync.User.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllUsersQuery implements Query<Void, List<UserGetAllDTO>> {
    private UserRepository userRepository;
    public  GetAllUsersQuery(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public ResponseEntity<List<UserGetAllDTO>> execute(Void input) {
        return ResponseEntity.ok(userRepository.findAll().stream().map(user -> new UserGetAllDTO(user)).toList());
    }
}
