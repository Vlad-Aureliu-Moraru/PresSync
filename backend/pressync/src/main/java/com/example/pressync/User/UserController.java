package com.example.pressync.User;

import com.example.pressync.User.CommandHandlers.CreateUserCommand;
import com.example.pressync.User.CommandHandlers.DeleteUserCommand;
import com.example.pressync.User.CommandHandlers.UpdateUserCommand;
import com.example.pressync.User.Model.DTOSs.UserGetDTO;
import com.example.pressync.User.Model.DTOSs.UserUpdateRequestDTO;
import com.example.pressync.User.Model.DTOSs.UserUpdateDTO;
import com.example.pressync.User.QueryHandlers.GetAllUsersQuery;
import com.example.pressync.User.QueryHandlers.GetUserByIdQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
    public ResponseEntity<List<UserGetDTO>> getAllUsers() {
        return getAllUsersQuery.execute(null);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR') or #id == principal.id")
    public ResponseEntity<UserGetDTO>getUserById(@PathVariable Integer id) {
        return getUserByIdQuery.execute(id);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteUser(@PathVariable Integer id) {
        return deleteUserCommand.execute(id);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody UserUpdateRequestDTO user) {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(id, user);
        return updateUserCommand.execute(userUpdateDTO);

    }
}
