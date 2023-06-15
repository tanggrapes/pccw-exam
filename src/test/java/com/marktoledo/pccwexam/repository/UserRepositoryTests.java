package com.marktoledo.pccwexam.repository;

import com.marktoledo.pccwexam.domain.User;
import com.marktoledo.pccwexam.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRepositoryTests {
    final String EMAIL = "mark@mark.com";
    @Autowired
    private UserRepository userRepository;


    @BeforeAll
    public void init() {
        log.info("STARTUP");
        User user = User.builder()
                .firstName("Mark")
                .lastName("Toledo")
                .email(EMAIL).build();
        userRepository.save(user);
    }


    @Test
    public void testGetUserByEmail() {
        User userByEmail = userRepository.getUserByEmail(EMAIL);

        Assertions.assertEquals(EMAIL, userByEmail.getEmail());

    }
}
