package com.jimmy.portal.user.service;


import com.jimmy.portal.auth.dto.UserDto;

import java.util.Optional;

public interface IUserService {


    Optional<UserDto> searchUserByEmail(String email);

    UserDto elevateToEmployer(Long userId);

    UserDto assignCompanyToEmployer(Long userId, Long companyId);

}