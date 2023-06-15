package com.marktoledo.pccwexam.controllers;

import com.marktoledo.pccwexam.annotations.ValidUUID;
import com.marktoledo.pccwexam.dto.UserDto;
import com.marktoledo.pccwexam.dto.UsersResponse;
import com.marktoledo.pccwexam.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/users")
@Validated
@CrossOrigin(origins = {"http://localhost:8080","http://localhost:4200"})
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(path = "/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable(value = "id") @ValidUUID String id) {
        return new ResponseEntity<>(userService.getUser(UUID.fromString(id)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UsersResponse> getUser(@RequestParam(value = "size") int size, @RequestParam(value = "page") int page) {
        return new ResponseEntity<>(userService.getUsers(size, page), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto user) {
        return new ResponseEntity<>(userService.registerUser(user), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> editUser(@Valid @RequestBody UserDto user, @PathVariable("id") @ValidUUID String id) {
        return new ResponseEntity<>(userService.editUser(user, UUID.fromString(id)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") @ValidUUID String id) {
        userService.deleteUser(UUID.fromString(id));
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUsers(@RequestParam("ids") @ValidUUID List<String> ids) {
        userService.deleteUsers(ids.stream().map(UUID::fromString).toList());
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
