package com.example.pressync.User;

import com.example.pressync.User.CommandHandlers.CreateUserCommand;
import com.example.pressync.User.CommandHandlers.DeleteUserCommand;
import com.example.pressync.User.CommandHandlers.UpdateUserCommand;
import com.example.pressync.User.Model.DTOs.LoginDTO;
import com.example.pressync.User.Model.User;
import com.example.pressync.User.Model.DTOs.UserCreateDTO;
import com.example.pressync.User.Model.DTOs.UserUpdateDTO;
import com.example.pressync.User.QueryHandlers.GetAllUsersQuery;
import com.example.pressync.User.QueryHandlers.GetUserByIdQuery;
import com.example.pressync.User.QueryHandlers.LoginQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final CreateUserCommand createUserCommand;
    private final GetAllUsersQuery getAllUsersQuery;
    private final UpdateUserCommand updateUserCommand;
    private final DeleteUserCommand deleteUserCommand;
    private final GetUserByIdQuery getUserByIdQuery;
    private final LoginQuery loginQuery;


    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return getAllUsersQuery.execute(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User>getUserById(@PathVariable Integer id) {
        return getUserByIdQuery.execute(id);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Integer id) {
        return deleteUserCommand.execute(id);
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody UserCreateDTO user) {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(id, user);
        return updateUserCommand.execute(userUpdateDTO);

    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
       return loginQuery.execute(loginDTO);
    }

}
