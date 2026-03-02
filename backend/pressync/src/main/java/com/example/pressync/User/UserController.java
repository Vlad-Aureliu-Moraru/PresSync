package com.example.pressync.User;

import com.example.pressync.User.CommandHandlers.CreateUserCommand;
import com.example.pressync.User.CommandHandlers.DeleteUserCommand;
import com.example.pressync.User.CommandHandlers.UpdateUserCommand;
import com.example.pressync.User.Model.User;
import com.example.pressync.User.Model.UserCreateDTO;
import com.example.pressync.User.Model.UserUpdateDTO;
import com.example.pressync.User.QueryHandlers.GetAllUsersQuery;
import com.example.pressync.User.QueryHandlers.GetUserByIdQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/User")
public class UserController {
    private CreateUserCommand createUserCommand;
    private GetAllUsersQuery getAllUsersQuery;
    private UpdateUserCommand updateUserCommand;
    private DeleteUserCommand deleteUserCommand;
    private GetUserByIdQuery getUserByIdQuery;
    public UserController(DeleteUserCommand deleteUserCommand,GetUserByIdQuery getUserByIdQuery,UpdateUserCommand updateUserCommand,CreateUserCommand createUserCommand, GetAllUsersQuery getAllUsersQuery) {
        this.createUserCommand = createUserCommand;
        this.getAllUsersQuery = getAllUsersQuery;
        this.updateUserCommand = updateUserCommand;
        this.deleteUserCommand = deleteUserCommand;
        this.getUserByIdQuery = getUserByIdQuery;
    }


    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserCreateDTO user) {
       return createUserCommand.execute(user);
    }
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

}
