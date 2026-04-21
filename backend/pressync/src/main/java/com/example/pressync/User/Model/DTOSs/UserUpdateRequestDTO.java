package com.example.pressync.User.Model.DTOSs;

import com.example.pressync.User.Model.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequestDTO {
    private String name;
    private String surname;
    private String email;
    private String password;
    private UserRoles role;
}
