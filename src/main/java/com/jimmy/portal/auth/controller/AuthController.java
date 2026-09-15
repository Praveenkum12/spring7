package com.jimmy.portal.auth.controller;

import com.jimmy.portal.auth.dto.LoginRequestDto;
import com.jimmy.portal.auth.dto.LoginResponseDto;
import com.jimmy.portal.auth.dto.RegisterRequestDto;
import com.jimmy.portal.auth.dto.UserDto;
import com.jimmy.portal.auth.entity.JobPortalUser;
import com.jimmy.portal.auth.entity.Role;
import com.jimmy.portal.auth.repository.JobPortalUserRepository;
import com.jimmy.portal.auth.repository.RoleRepository;
import com.jimmy.portal.constant.ApplicationConstant;
import com.jimmy.portal.security.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final CompromisedPasswordChecker compromisedPasswordChecker;

    private final JobPortalUserRepository jobPortalUserRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    @PostMapping("/login/public")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {

       try {
           var resultAuthentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.username(), loginRequestDto.password()));
           String accessToken =  jwtUtil.generateJwtToken(resultAuthentication);

           JobPortalUser loggedInUser = (JobPortalUser) resultAuthentication.getPrincipal();
           UserDto user = new UserDto();
           BeanUtils.copyProperties(loggedInUser, user);
           user.setRole(loggedInUser.getRole().getName());
           user.setUserId(loggedInUser.getId());

           return ResponseEntity.ok().body(new LoginResponseDto(HttpStatus.OK.getReasonPhrase(), user, accessToken));
       } catch(BadCredentialsException bce) {
           return buildErrorResponse(HttpStatus.UNAUTHORIZED , "Invalid username or password");
       } catch (AuthenticationException ae) {
           return  buildErrorResponse(HttpStatus.UNAUTHORIZED , "Authentication failed");
       } catch (Exception e) {
           return  buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR , "Something went wrong");
       }
    }

    @PostMapping(value = "/register/public",version = "1.0")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDto registerRequestDto) {

        JobPortalUser jobPortalUser = new JobPortalUser();
        BeanUtils.copyProperties(registerRequestDto, jobPortalUser);
        jobPortalUser.setPassword(passwordEncoder.encode(registerRequestDto.password()));

        Role role = roleRepository.findByName(ApplicationConstant.ROLE_JOB_SEEKER)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " +
                        ApplicationConstant.ROLE_JOB_SEEKER));
        jobPortalUser.setRole(role);
        jobPortalUserRepository.save(jobPortalUser);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    private ResponseEntity<LoginResponseDto> buildErrorResponse(HttpStatus status,
                                                                String message) {
        return ResponseEntity
                .status(status)
                .body(new LoginResponseDto(message, null, null));
    }

}
