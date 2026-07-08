package com.example.pressync.User.Model.DTOSs;

import com.example.pressync.User.Model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserGetDTO {
    private int id;
    private String name;
    private String surname;
    private String email;
    private String role;
    private boolean active;
    public UserGetDTO(User user) {
        this.active = user.getActive();
        this.id = user.getId();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.email = maskEmail(user.getEmail());
        this.role = user.getRole().toString();
    }

    private static String maskEmail(String email) {
        if (email == null || !email.contains("@")) return email;
        int atIndex = email.indexOf('@');
        if (atIndex <= 1) return email;
        return email.charAt(0) + "****" + email.substring(atIndex - 1);
    }
}
