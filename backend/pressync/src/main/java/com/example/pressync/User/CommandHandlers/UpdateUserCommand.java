package com.example.pressync.User.CommandHandlers;

import com.example.pressync.Command;
import com.example.pressync.User.Model.User;
import com.example.pressync.User.Model.UserCreateDTO;
import com.example.pressync.User.Model.UserUpdateDTO;
import com.example.pressync.User.UserRepository;
import com.example.pressync.User.Validator.UserValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UpdateUserCommand implements Command<UserUpdateDTO,String> {
    private final UserValidator userValidator;
    private UserRepository userRepository;
    public UpdateUserCommand(UserRepository userRepository, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }
    @Override
    public ResponseEntity<String> execute(UserUpdateDTO entity) {
        int id = entity.getId();
        UserCreateDTO entityUser = entity.getUser();
        User toUpdate = new User();

        User foundUser = userRepository.findById(id).orElse(null);

        if (foundUser == null) {
           throw new IllegalArgumentException("User does not exist");
        }
        userValidator.validate(entityUser.getName(),entityUser.getSurname(),entityUser.getEmail(),id);

        toUpdate.setId(id);
        toUpdate.setName(entityUser.getName());
        toUpdate.setSurname(entityUser.getSurname());
        toUpdate.setEmail(entityUser.getEmail());
        toUpdate.setPassword(foundUser.getPassword());
        toUpdate.setRole(foundUser.getRole());
        toUpdate.setActive(foundUser.getActive());

        userRepository.save(toUpdate);


        return ResponseEntity.ok().build();

    }
}
