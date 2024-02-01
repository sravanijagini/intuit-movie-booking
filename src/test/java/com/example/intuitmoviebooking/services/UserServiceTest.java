package com.example.intuitmoviebooking.services;

import com.example.intuitmoviebooking.model.User;
import com.example.intuitmoviebooking.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testGetUserById() {
        String userMailId = "test@example.com";
        User user = new User();
        user.setUserName("Test User");
        user.setMailId(userMailId);
        when(userRepository.findAll(userMailId)).thenReturn(Arrays.asList(user));

        List<User> result = userService.getUserById(userMailId);

        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
    }

    @Test
    public void testAddNewUser_Success() {
        String userMailId = "newuser@example.com";
        User newUser = new User();
        newUser.setUserName("New User");
        newUser.setMailId(userMailId);
        when(userRepository.findAll(userMailId)).thenReturn(Arrays.asList());

        boolean result = userService.addNewUser(newUser);

        assertFalse(result);
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    public void testAddNewUser_Duplicate() {
        String existingUserMailId = "abc@123";
        User existingUser = new User();
        existingUser.setUserName("sravani");
        existingUser.setMailId(existingUserMailId);
        when(userRepository.findAll(existingUserMailId)).thenReturn(Arrays.asList(existingUser));
        boolean result = userService.addNewUser(existingUser);

        assertTrue(result);
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testAdduserIntoDB() {
        User user = new User();
        user.setUserName("Test User");
        user.setMailId("test@example.com");

        userService.adduserIntoDB(user);

        verify(userRepository, times(1)).save(user);
    }
}
