package com.marktoledo.pccwexam.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private UUID id;

    private Date creationDate;

    @NotNull(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "First Name is required")
    @Size(min = 2, max = 50, message = "First Name must be between 2 to 50 characters")
    private String firstName;

    @Size(min = 2, max = 50, message = "Middle Name must be between 2 to 50 characters")
    private String middleName;

    @NotNull(message = "Last Name is required")
    @Size(min = 2, max = 50, message = "Last Name must be between 2 to 50 characters")
    @Column(nullable = false)
    private String lastName;
}
