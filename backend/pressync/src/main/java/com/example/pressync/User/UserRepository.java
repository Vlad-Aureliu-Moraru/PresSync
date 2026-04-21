package com.example.pressync.User;


import com.example.pressync.User.Model.User;
import com.example.pressync.User.Model.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    boolean existsByEmail(String email);
    boolean existsByRole(UserRoles role);
    Optional<User> findByEmail(String email);
}
