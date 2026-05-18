package com.example.pressync.User.QueryHandlers;

import com.example.pressync.Query;
import com.example.pressync.User.Model.DTOSs.UserGetDTO;
import com.example.pressync.User.Model.User;
import com.example.pressync.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserByIdQuery implements Query<Integer, UserGetDTO> {
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<UserGetDTO> execute(Integer input) {
        User found = userRepository.findById(input).orElse(null);
        if (found == null) {
            throw new IllegalArgumentException("User with id " + input + " not found");
        }
        return ResponseEntity.ok().body(new UserGetDTO(found));
    }
}
