package com.example.pressync.User.QueryHandlers;

import com.example.pressync.Query;
import com.example.pressync.User.Model.DTOSs.UserGetDTO;
import com.example.pressync.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllUsersQuery implements Query<Void, List<UserGetDTO>> {
    private final UserRepository userRepository;
    @Override
    public ResponseEntity<List<UserGetDTO>> execute(Void input) {
        return ResponseEntity.ok(userRepository.findAll().stream().map(user -> new UserGetDTO(user)).toList());
    }
}
