package com.marktoledo.pccwexam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marktoledo.pccwexam.controllers.UserController;
import com.marktoledo.pccwexam.domain.User;
import com.marktoledo.pccwexam.dto.UserDto;
import com.marktoledo.pccwexam.dto.UsersResponse;
import com.marktoledo.pccwexam.services.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;
    private UserDto userDto;
    private User user;

    @BeforeEach
    public void init() {
        user = User.builder()
                .email("mark@mark.com")
                .firstName("mark")
                .lastName("toledo").build();
        userDto = UserDto.builder()
                .email("mark@mark.com")
                .firstName("mark")
                .lastName("toledo").build();

    }

    @Test
    public void testRegisterUser() throws Exception {
        given(userService.registerUser(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));
        ResultActions response = mockMvc.perform(
                post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)));
        response.andExpect(MockMvcResultMatchers.status().isCreated());

    }

    @Test
    public void testGetUsers() throws Exception {
        UsersResponse usersResponse = UsersResponse.builder()
                .last(true).pageNo(0).pageSize(10).content(Arrays.asList(userDto)).build();
        when(userService.getUsers(10, 0)).thenReturn(usersResponse);

        ResultActions response = mockMvc.perform(get("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "10"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.size()",
                        CoreMatchers.is(usersResponse.getContent().size())));

    }

    @Test
    public void testGetUser() throws Exception {
        UUID id = UUID.randomUUID();
        when(userService.getUser(id)).thenReturn(userDto);
        ResultActions response = mockMvc.perform(get("/api/v1/users/" + id)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(userDto.getEmail())));

    }

    @Test
    public void testEditUser() throws Exception {
        UUID id = UUID.randomUUID();
        when(userService.editUser(userDto, id)).thenReturn(userDto);
        ResultActions response = mockMvc.perform(put("/api/v1/users/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(userDto.getEmail())));

    }

    @Test
    public void testDeleteUser() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(userService).deleteUser(id);
        ResultActions response = mockMvc.perform(delete("/api/v1/users/" + id)
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    public void testDeleteUsers() throws Exception {
        List<UUID> ids = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        doNothing().when(userService).deleteUsers(ids);
        ResultActions response = mockMvc.perform(delete("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .param("ids", String.join(",", ids.stream().map(UUID::toString).collect(Collectors.joining(",")))));
        response.andExpect(MockMvcResultMatchers.status().isNoContent());

    }


}
