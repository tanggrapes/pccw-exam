package com.marktoledo.pccwexam.service;

import com.marktoledo.pccwexam.domain.User;
import com.marktoledo.pccwexam.dto.UserDto;
import com.marktoledo.pccwexam.dto.UsersResponse;
import com.marktoledo.pccwexam.exceptions.EmailAddressExistException;
import com.marktoledo.pccwexam.repositories.UserRepository;
import com.marktoledo.pccwexam.services.impl.UserServiceImpl;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JavaMailSender mailSender;


    @Test
    public void testRegisterUser() {
        User user = User.builder()
                .email("test@test.com")
                .lastName("test")
                .middleName("middle")
                .firstName("test").build();
        UserDto userDto = UserDto.builder()
                .email("test@test.com")
                .lastName("test")
                .middleName("middle")
                .firstName("test").build();
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        when(mailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        UserDto createdUser = userService.registerUser(userDto);
        Assertions.assertNotNull(createdUser);
    }

    @Test
    public void testRegisterUserWithExistingEmail() {
        Assertions.assertThrows(EmailAddressExistException.class, () -> {
            User user = User.builder()
                    .email("test@test.com")
                    .lastName("test")
                    .middleName("middle")
                    .firstName("test").build();
            UserDto userDto = UserDto.builder()
                    .email("test@test.com")
                    .lastName("test")
                    .middleName("middle")
                    .firstName("test").build();
            when(userRepository.getUserByEmail("test@test.com")).thenReturn(user);
            UserDto createdUser = userService.registerUser(userDto);
        });
    }

    @Test
    public void testEditUser() {
        UUID uuid = UUID.randomUUID();
        User user = User.builder()
                .id(uuid)
                .email("test@test.com")
                .lastName("test")
                .middleName("middle")
                .firstName("test").build();
        UserDto userDto = UserDto.builder()
                .email("test@test.com")
                .lastName("test")
                .middleName("middle")
                .firstName("test").build();
        when(userRepository.findById(uuid)).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(user)).thenReturn(user);
        UserDto updated = userService.editUser(userDto, uuid);
        Assertions.assertNotNull(updated);
    }

    @Test
    public void testDeleteUser() {
        UUID uuid = UUID.randomUUID();
        doNothing().when(userRepository).deleteById(uuid);
        Assertions.assertAll(() -> userService.deleteUser(uuid));
    }

    @Test
    public void testDeleteUsers() {
        List<UUID> ids = Arrays.asList(UUID.randomUUID(),UUID.randomUUID());
        doNothing().when(userRepository).deleteAllById(ids);
        Assertions.assertAll(() -> userService.deleteUsers(ids));
    }

    @Test
    public void testGetUser(){
        UUID uuid = UUID.randomUUID();
        User user = User.builder()
                .id(uuid)
                .email("test@test.com")
                .lastName("test")
                .middleName("middle")
                .firstName("test").build();
        when(userRepository.findById(uuid)).thenReturn(Optional.ofNullable(user));
        UserDto result = userService.getUser(uuid);
        Assertions.assertNotNull(result);
    }

    @Test
    public void testGetUsers(){
        Page<User>  users = Mockito.mock(Page.class);
        when(userRepository.findAll(Mockito.any(Pageable.class))).thenReturn(users);
        UsersResponse pageUsersResult = userService.getUsers(10,0);
        Assertions.assertNotNull(pageUsersResult);
    }
}
