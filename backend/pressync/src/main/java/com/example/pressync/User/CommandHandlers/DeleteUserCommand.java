package com.example.pressync.User.CommandHandlers;

import com.example.pressync.Command;
import com.example.pressync.User.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DeleteUserCommand implements Command<Integer,String> {
    private UserRepository userRepository;
    public DeleteUserCommand(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public ResponseEntity<String> execute(Integer entity) {
        if (!userRepository.existsById(entity)) {
            throw new IllegalArgumentException("User does not exist");
        }
        userRepository.deleteById(entity);
        return ResponseEntity.ok().build();
    }
}
