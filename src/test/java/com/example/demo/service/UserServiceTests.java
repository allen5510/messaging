package com.example.demo.service;

import com.example.demo.controller.DTO.UserDTO;
import com.example.demo.controller.util.RegexType;
import com.example.demo.exception.*;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTests {
    private final long TEST_USER_ID = 1L;
    private final String TEST_ACCOUNT = "test@gmail.com";
    private final String TEST_PASSWORD = "1234qwer";
    private final String TEST_USERNAME = "test";

    private final long NOT_EXISTS_USER_ID = 2L;
    private final String NOT_EXISTS_ACCOUNT = "test2@gmail.com";


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserEntity mockUser;
    private UserDTO mockUserDTO;

    @BeforeEach
    public void setUp() {
        mockUser = new UserEntity(TEST_ACCOUNT, TEST_PASSWORD, TEST_USERNAME);
        mockUserDTO = UserDTO.createByEntity(mockUser);
        when(userRepository.existsById(TEST_USER_ID)).thenReturn(true);
        when(userRepository.getById(TEST_USER_ID)).thenReturn(mockUser);
        when(userRepository.existsByAccount(TEST_ACCOUNT)).thenReturn(true);
        when(userRepository.findByAccount(TEST_ACCOUNT)).thenReturn(Optional.of(mockUser));
    }

    @Test
    public void testValidateUserExistence(){
        assertDoesNotThrow(() -> userService.validateUserExistence(TEST_USER_ID));
    }

    @Test
    public void testValidateUserExistence_Error(){
        assertThrows(NotFoundException.class, () -> {
            userService.validateUserExistence(NOT_EXISTS_USER_ID);
        });
    }

    @Test
    public void testIsAccountExists_returnTrue() {
        assertTrue(userRepository.existsByAccount(TEST_ACCOUNT));
    }

    @Test
    public void testIsAccountExists_returnFalse() {
        assertFalse(userRepository.existsByAccount(NOT_EXISTS_ACCOUNT));
    }

    @Test
    public void testGetUserDTO() {
        UserDTO userDTO = userService.getUserDTO(TEST_USER_ID);
        assertUserEquals(mockUserDTO, userDTO);
    }

    @Test
    public void testRegister_successful() {
        final String NEW_ACCOUNT = "new@gmail.com";
        final String NEW_PASSWORD = "1234qwer";
        final String NEW_USERNAME = "newAccount";
        assertDoesNotThrow(()->{
            UserDTO userDTO = userService.register(NEW_ACCOUNT, NEW_PASSWORD, NEW_USERNAME);
            assertEquals(NEW_ACCOUNT, userDTO.getAccount());
            assertEquals(NEW_USERNAME, userDTO.getUsername());
            verify(userRepository).save(Mockito.any(UserEntity.class));
        });
    }

    @Test
    public void testRegister_duplicate() {
        assertThrows(DuplicateException.class, () -> {
            userService.register(TEST_ACCOUNT, "1234qwer", "newAccount");
        });
    }

    @Test
    public void testLogin_successful() {
        assertDoesNotThrow(() -> {
            UserDTO userDTO = userService.login(TEST_ACCOUNT, TEST_PASSWORD);
            assertUserEquals(mockUserDTO, userDTO);
        });
    }

    @Test
    public void testLogin_passwordError() {
        assertThrows(LoginException.class, () -> {
            userService.login(TEST_ACCOUNT, "wrongPassword");
        });
    }

    @Test
    public void testChangePassword_successful() {
        assertDoesNotThrow(() -> {
            userService.changePassword(TEST_USER_ID, TEST_PASSWORD, "newPassword1234");
            verify(userRepository).save(mockUser);
        });
    }


    @Test
    public void testChangePassword_passwordError() {
        assertThrows(LoginException.class, () -> {
            userService.changePassword(TEST_USER_ID, "wrong password", "newPassword1234");
        });
    }

    @Test
    public void testChangePassword_newPasswordFormatError() {
        assertThrows(FormatException.class, () -> {
            userService.changePassword(TEST_USER_ID, TEST_PASSWORD, "1234");
        });
    }

    @Test
    public void testUpdateUsername() {
        final String NEW_NAME = "newUsername";
        userService.updateUsername(TEST_USER_ID, NEW_NAME);
        verify(userRepository).updateUsernameById(TEST_USER_ID, NEW_NAME);
    }

    private void assertUserEquals(UserDTO expected, UserDTO actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getAccount(), actual.getAccount());
        assertEquals(expected.getUsername(), actual.getUsername());
    }
}