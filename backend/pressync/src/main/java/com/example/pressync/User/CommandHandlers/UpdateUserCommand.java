package com.example.pressync.User.CommandHandlers;

import com.example.pressync.Command;
import com.example.pressync.User.Model.User;
import com.example.pressync.User.Model.DTOSs.UserUpdateRequestDTO;
import com.example.pressync.User.Model.UserRoles;
import com.example.pressync.User.Model.DTOSs.UserUpdateDTO;
import com.example.pressync.User.UserRepository;
import com.example.pressync.User.Validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUserCommand implements Command<UserUpdateDTO,String> {
    private static final Logger auditLogger = LoggerFactory.getLogger("com.example.pressync.audit");

    private final UserValidator userValidator;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<String> execute(UserUpdateDTO entity) {
        int id = entity.getId();
        UserUpdateRequestDTO entityUser = entity.getUser();
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
        boolean passwordChanged = entityUser.getPassword() != null && !entityUser.getPassword().isBlank();
        toUpdate.setPassword(passwordChanged ? passwordEncoder.encode(entityUser.getPassword()) : foundUser.getPassword());
        UserRoles updatedRole = resolveUpdatedRole(foundUser, entityUser);
        toUpdate.setRole(updatedRole);
        toUpdate.setActive(foundUser.getActive());
        toUpdate.setFailedLoginAttempts(foundUser.getFailedLoginAttempts());
        toUpdate.setAccountLockedUntil(foundUser.getAccountLockedUntil());
        toUpdate.setMfaEnabled(foundUser.getMfaEnabled());
        toUpdate.setMfaCode(foundUser.getMfaCode());
        toUpdate.setMfaExpiry(foundUser.getMfaExpiry());

        userRepository.save(toUpdate);

        if (passwordChanged) {
            auditLogger.info("action=PASSWORD_CHANGED actorId={} targetUserId={}", getActorId(), toUpdate.getId());
        }
        if (updatedRole != foundUser.getRole()) {
            auditLogger.info(
                    "action=ROLE_CHANGED actorId={} targetUserId={} oldRole={} newRole={}",
                    getActorId(),
                    toUpdate.getId(),
                    foundUser.getRole(),
                    updatedRole
            );
        }


        return ResponseEntity.ok().build();

    }

    private UserRoles resolveUpdatedRole(User foundUser, UserUpdateRequestDTO entityUser) {
        if (entityUser.getRole() == null) {
            return foundUser.getRole();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return foundUser.getRole();
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User currentUser)) {
            return foundUser.getRole();
        }

        if (currentUser.getRole() != UserRoles.ADMIN) {
            return foundUser.getRole();
        }

        return entityUser.getRole();
    }

    private Integer getActorId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof User currentUser) {
            return currentUser.getId();
        }

        return null;
    }
}
