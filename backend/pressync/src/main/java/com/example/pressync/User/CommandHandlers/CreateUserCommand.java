package com.example.pressync.User.CommandHandlers;

import com.example.pressync.Command;
import com.example.pressync.User.Model.User;
import com.example.pressync.User.Model.DTOs.UserCreateDTO;
import com.example.pressync.User.Model.UserRoles;
import com.example.pressync.User.UserRepository;
import com.example.pressync.User.Validator.UserValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateUserCommand implements Command<UserCreateDTO,String> {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserValidator userValidator;
    private final UserRepository userRepository;
    public CreateUserCommand(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userValidator = userValidator;
    }
    @Override
    public ResponseEntity<String> execute(UserCreateDTO entity) {

        userValidator.validate(entity.getName(),entity.getSurname(),entity.getEmail());

        User userToSave = new User();
        userToSave.setName(entity.getName());
        userToSave.setEmail(entity.getEmail());
        userToSave.setSurname(entity.getSurname());

        userToSave.setActive(true);
        userToSave.setRole(UserRoles.USER);


        String hashedPassword = passwordEncoder.encode(entity.getPassword());
        userToSave.setPassword(hashedPassword);

        userRepository.save(userToSave);

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
