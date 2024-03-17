package com.example.demo.controller;

import com.example.demo.controller.DTO.UserDTO;
import com.example.demo.exception.*;
import com.example.demo.service.UserService;
import com.example.demo.controller.util.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTests {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @BeforeEach
    void setUp() {
        userController = new UserController(userService);
    }

    @Test
    void testRegister_SuccessfulRegistration() throws Exception {
        // Arrange
        long userId = 1L;
        String account = "test@example.com";
        String password = "Abcd1234";
        String username = "John Doe";

        UserDTO userDTO = new UserDTO(userId, account, username);
        when(userService.register(account, password, username)).thenReturn(userDTO);

        MvcResult result = mockMvc
                .perform(post("/account/register")
                        .param(RequestKeys.ACCOUNT, account)
                        .param(RequestKeys.PASSWORD, password)
                        .param(RequestKeys.USERNAME, username))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rc").value(0))
                .andExpect(jsonPath("$.user").exists())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONObject json = new JSONObject(content);
        JSONObject userJson = json.getJSONObject("user");
        assertUserEquals(userDTO, userJson);
    }

    private void assertUserEquals(UserDTO expected, JSONObject actualJson) {
        try {
            assertEquals(expected.getId(), actualJson.getLong("id"));
            assertEquals(expected.getAccount(), actualJson.getString("account"));
            assertEquals(expected.getUsername(), actualJson.get("username"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testRegister_AccountAlreadyExists() throws FormatException, DuplicateException {
        // Arrange
        String account = "test@example.com";
        String password = "Abcd1234";
        String username = "John Doe";

        when(userService.register(account, password, username))
                .thenThrow(new DuplicateException("account already exists"));

        // Act & Assert
        assertThrows(DuplicateException.class, () -> {
            userController.register(account, password, username);
            verify(userService, never()).register(anyString(), anyString(), anyString());
        });
    }

}