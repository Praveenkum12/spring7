package com.jimmy.portal.auth.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UserDto {

    private Long userId;
    private String name;
    private String email;
    private String mobileNumber;
    private String role;
    private String companyName;
    private Long companyId;
    private Instant createdAt;

}
