package edu.java.springsecuritytask.service;

import edu.java.springsecuritytask.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class})
class UserServiceTest {

    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
        userService = new UserService(userRepository);
    }

    @Test
    void findByUserName() {
        userService.findByUserName(anyString());

        verify(userRepository).findByUsername(anyString());
    }

    @Test
    void findByUsernameAndPassword() {
        userService.findByUsernameAndPassword(anyString(), anyString());

        verify(userRepository).findByUsernameAndPassword(anyString(), anyString());
    }

    @Test
    void changePassword() {
    }
}