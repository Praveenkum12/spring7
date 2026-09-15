package com.jimmy.portal.auth.dto;

public record LoginResponseDto(String message, UserDto user, String jwt) {
}
