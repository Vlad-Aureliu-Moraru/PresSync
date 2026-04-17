package com.example.pressync.User.QueryHandlers;

import com.example.pressync.Query;
import com.example.pressync.User.Model.DTOSs.UserGetDTO;
import com.example.pressync.User.Model.User;
import com.example.pressync.User.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GetUserByIdQuery implements Query<Integer, UserGetDTO> {
    private UserRepository userRepository;

    public GetUserByIdQuery(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<UserGetDTO> execute(Integer input) {
        User found = userRepository.findById(input).orElse(null);
        if (found == null) {
            throw new IllegalArgumentException("User with id " + input + " not found");
        }
        return ResponseEntity.ok().body(new UserGetDTO(found));
    }
}
