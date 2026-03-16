package com.example.pressync.User.Model.DTOs;

import com.example.pressync.User.Model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserGetAllDTO {
    private int id;
    private String name;
    private String surname;
    private String email;
    private String role;
    private boolean active;
    public UserGetAllDTO(User user) {
        this.active = user.getActive();
        this.id = user.getId();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.email = user.getEmail();
        this.role = user.getRole().toString();
    }
}
