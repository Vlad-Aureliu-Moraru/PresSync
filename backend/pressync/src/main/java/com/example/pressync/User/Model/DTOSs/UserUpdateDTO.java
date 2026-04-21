package com.example.pressync.User.Model.DTOSs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {
    private int id;
    private UserUpdateRequestDTO user;
}
