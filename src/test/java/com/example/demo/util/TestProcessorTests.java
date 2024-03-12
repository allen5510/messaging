package com.example.demo.util;
import com.example.demo.controller.DTO.UserDTO;
import com.example.demo.exception.TokenException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestProcessorTests {
    @Test
    void testGenerateTokenAndParseUsersno() throws TokenException {
        // Arrange
        UserDTO user = new UserDTO(12345, "testUser@gmail.com", "testUser");
        // Act
        String token = TokenProcessor.generateTokenAndThrow(user);
        long usersno = TokenProcessor.parseUserIdAndThrow(token);
        // Assert
        Assertions.assertEquals(user.getId(), usersno);
    }

    @Test
    void testGenerateTokenAndParseUsersno_InvalidToken() {
        // Arrange
        String invalidToken = "abcd1234";
        // Act & Assert
        Assertions.assertThrows(TokenException.class, () -> {
            TokenProcessor.parseUserIdAndThrow(invalidToken);
        });
    }
}