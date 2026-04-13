package com.example.pressync.User.CommandHandlers;

import com.example.pressync.User.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUserCommandTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DeleteUserCommand deleteUserCommand;

    @Test
    void testDeleteUserSuccess() {
        int id = 1;
        when(userRepository.existsById(id)).thenReturn(true);

        ResponseEntity<String> response = deleteUserCommand.execute(id);

        assertEquals(200, response.getStatusCode().value());
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteUserNotFound() {
        int id = 1;
        when(userRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> deleteUserCommand.execute(id));

        assertEquals("User does not exist", exception.getMessage());
        verify(userRepository, never()).deleteById(anyInt());
    }
}
