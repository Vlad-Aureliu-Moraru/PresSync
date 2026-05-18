package com.example.pressync.User.CommandHandlers;

import com.example.pressync.Command;
import com.example.pressync.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteUserCommand implements Command<Integer,String> {
    private final UserRepository userRepository;
    @Override
    public ResponseEntity<String> execute(Integer entity) {
        if (!userRepository.existsById(entity)) {
            throw new IllegalArgumentException("User does not exist");
        }
        userRepository.deleteById(entity);
        return ResponseEntity.ok().build();
    }
}
