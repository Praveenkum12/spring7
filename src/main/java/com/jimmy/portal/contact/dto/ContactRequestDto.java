package com.jimmy.portal.contact.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ContactRequestDto(

        @Email(message = "Enter a valid email")
        @NotBlank(message = "Email can't be empty")
        String email,

        @NotBlank(message = "Subject can't be empty")
        String subject,

        @NotBlank(message = "Message can't be empty")
        String message,

        @Pattern(regexp = "Job_Seeker|Employer|Other", message = "Usertype should be: Job_Seeker|Employer|Other")
        @NotBlank(message = "UserType cannot be empty")
        String userType,

        @NotBlank(message = "Name cannot be empty")
        String name
) {
}