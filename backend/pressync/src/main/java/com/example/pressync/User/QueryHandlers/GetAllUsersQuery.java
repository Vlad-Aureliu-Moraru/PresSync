package com.example.pressync.User.QueryHandlers;

import com.example.pressync.Query;
import com.example.pressync.User.Model.DTOSs.UserGetDTO;
import com.example.pressync.User.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllUsersQuery implements Query<Void, List<UserGetDTO>> {
    private UserRepository userRepository;
    public  GetAllUsersQuery(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public ResponseEntity<List<UserGetDTO>> execute(Void input) {
        return ResponseEntity.ok(userRepository.findAll().stream().map(user -> new UserGetDTO(user)).toList());
    }
}
