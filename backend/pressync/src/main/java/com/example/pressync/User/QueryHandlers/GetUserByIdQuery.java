package com.example.pressync.User.QueryHandlers;

import com.example.pressync.Query;
import com.example.pressync.User.Model.User;
import com.example.pressync.User.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GetUserByIdQuery implements Query<Integer, User> {
    private UserRepository userRepository;

    public GetUserByIdQuery(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<User> execute(Integer input) {
        User found = userRepository.findById(input).orElse(null);
        if (found == null) {
            throw new IllegalArgumentException("User with id " + input + " not found");
        }
        return ResponseEntity.ok().body(found);
    }
}
