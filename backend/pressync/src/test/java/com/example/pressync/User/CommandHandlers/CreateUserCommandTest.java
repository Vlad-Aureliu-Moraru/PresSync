package com.example.pressync.User.CommandHandlers;

import com.example.pressync.Services.Auth.AuthenticationResponse;
import com.example.pressync.Services.Auth.JWTService;
import com.example.pressync.User.Model.DTOSs.UserCreateDTO;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserCommandTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserValidator userValidator;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JWTService jwtService;

    @InjectMocks
    private CreateUserCommand createUserCommand;

    private UserCreateDTO createDTO;

    @BeforeEach
    void setUp() {
        createDTO = new UserCreateDTO();
        createDTO.setName("John");
        createDTO.setSurname("Doe");
        createDTO.setEmail("john.doe@example.com");
        createDTO.setPassword("password123");
    }

    @Test
    void testCreateUserSuccess() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("mockToken");

        ResponseEntity<AuthenticationResponse> response = createUserCommand.execute(createDTO);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("mockToken", response.getBody().token());
        verify(userValidator, times(1)).validate(anyString(), anyString(), anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUserValidationFail() {
        doThrow(new IllegalArgumentException("Invalid email")).when(userValidator)
                .validate(anyString(), anyString(), anyString());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> createUserCommand.execute(createDTO));

        assertEquals("Invalid email", exception.getMessage());
        verify(userRepository, never()).save(any());
    }
}
