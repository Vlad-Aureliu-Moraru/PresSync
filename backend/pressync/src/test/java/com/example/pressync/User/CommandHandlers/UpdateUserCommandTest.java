package com.example.pressync.User.CommandHandlers;

import com.example.pressync.User.Model.DTOSs.UserUpdateDTO;
import com.example.pressync.User.Model.DTOSs.UserUpdateRequestDTO;
import com.example.pressync.User.Model.User;
import com.example.pressync.User.UserRepository;
import com.example.pressync.User.Validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserCommandTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserValidator userValidator;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UpdateUserCommand updateUserCommand;

    private UserUpdateDTO updateDTO;
    private UserUpdateRequestDTO updateRequestDTO;
    private User foundUser;

    @BeforeEach
    void setUp() {
        updateRequestDTO = new UserUpdateRequestDTO();
        updateRequestDTO.setName("John");
        updateRequestDTO.setSurname("Doe");
        updateRequestDTO.setEmail("john.doe@example.com");

        updateDTO = new UserUpdateDTO(1, updateRequestDTO);

        foundUser = new User();
        foundUser.setId(1);
        foundUser.setPassword("oldPassword");
        foundUser.setActive(true);
    }

    @Test
    void testUpdateUserSuccess() {
        when(userRepository.findById(1)).thenReturn(Optional.of(foundUser));

        ResponseEntity<String> response = updateUserCommand.execute(updateDTO);

        assertEquals(200, response.getStatusCode().value());
        verify(userValidator, times(1)).validate(eq("John"), eq("Doe"), eq("john.doe@example.com"), eq(1));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> updateUserCommand.execute(updateDTO));

        assertEquals("User does not exist", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdateUserValidationFail() {
        when(userRepository.findById(1)).thenReturn(Optional.of(foundUser));
        doThrow(new IllegalArgumentException("Invalid email")).when(userValidator)
                .validate(anyString(), anyString(), anyString(), anyInt());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> updateUserCommand.execute(updateDTO));

        assertEquals("Invalid email", exception.getMessage());
        verify(userRepository, never()).save(any());
    }
}
