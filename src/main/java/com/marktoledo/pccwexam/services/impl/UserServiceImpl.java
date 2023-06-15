package com.marktoledo.pccwexam.services.impl;

import com.marktoledo.pccwexam.domain.User;
import com.marktoledo.pccwexam.dto.UserDto;
import com.marktoledo.pccwexam.dto.UsersResponse;
import com.marktoledo.pccwexam.exceptions.EmailAddressExistException;
import com.marktoledo.pccwexam.exceptions.UserNotFoundException;
import com.marktoledo.pccwexam.repositories.UserRepository;
import com.marktoledo.pccwexam.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private JavaMailSender mailSender;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    @Override
    public UserDto registerUser(UserDto userDto) {


//        it will throw EmailAddressExistException
        checkIfEmailExist(userDto.getEmail());

        User createdUser = new User();
        createdUser.setEmail(userDto.getEmail());
        createdUser.setFirstName(userDto.getFirstName());
        createdUser.setMiddleName(userDto.getMiddleName());
        createdUser.setLastName(userDto.getLastName());
        createdUser.setCreationDate(new Date());
        createdUser = userRepository.save(createdUser);
        MimeMessage message = mailSender.createMimeMessage();

        try {
            message.setFrom(new InternetAddress("sender@example.com"));
            message.setRecipients(MimeMessage.RecipientType.TO, createdUser.getEmail());
            message.setSubject(String.format("Welcome %s %s!", createdUser.getFirstName(), createdUser.getLastName()));

            String htmlContent = String.format("<h1>Welcome %s %s!</h1>", createdUser.getFirstName(), createdUser.getLastName()) +
                    "<p>Thank you for creating user to our system</p>";
            message.setContent(htmlContent, "text/html; charset=utf-8");

            mailSender.send(message);
        } catch (MessagingException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        BeanUtils.copyProperties(createdUser, userDto);
        return userDto;
    }

    @Override
    public UserDto editUser(UserDto userDto, UUID id) {
        User userToUpdate = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found!"));

//        check email if the same to user to be update if not same check if Email exist
        if (!Objects.equals(userToUpdate.getEmail(), userDto.getEmail())) {
            checkIfEmailExist(userDto.getEmail());
            userToUpdate.setEmail(userDto.getEmail());
        }

        userToUpdate.setFirstName(userDto.getFirstName());
        userToUpdate.setMiddleName(userDto.getMiddleName());
        userToUpdate.setLastName(userDto.getLastName());

        BeanUtils.copyProperties(userRepository.save(userToUpdate), userDto);
        return userDto;
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);

    }

    @Override
    public void deleteUsers(List<UUID> ids) {
        userRepository.deleteAllById(ids);
    }

    @Override
    public UsersResponse getUsers(int pageSize, int page) {
        Page<User> users = userRepository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "creationDate")));
        List<User> listOfUser = users.getContent();
        List<UserDto> content = listOfUser.stream().map(u -> {
            UserDto dto = new UserDto();
            BeanUtils.copyProperties(u, dto);
            return dto;
        }).collect(Collectors.toList());

        return UsersResponse.builder()
                .content(content)
                .pageNo(users.getNumber())
                .pageSize(users.getSize())
                .totalElements(users.getTotalElements())
                .totalPages(users.getTotalPages())
                .last(users.isLast()).build();
    }

    @Override
    public UserDto getUser(UUID id) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found!")), userDto);
        return userDto;
    }

    private void checkIfEmailExist(String email) {
        User userWithSameEmail = userRepository.getUserByEmail(email);

        if (userWithSameEmail != null) {
            throw new EmailAddressExistException("Email already taken");
        }
    }


}
