package com.marktoledo.pccwexam.services;

import com.marktoledo.pccwexam.dto.UserDto;
import com.marktoledo.pccwexam.dto.UsersResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserDto registerUser(UserDto user);

    UserDto editUser(UserDto user, UUID id);

    void deleteUser(UUID id);

    void deleteUsers(List<UUID> ids);

    UsersResponse getUsers(int pageSize, int page);

    UserDto getUser(UUID id);

}
