package com.jimmy.portal.base;

import java.time.LocalDateTime;

public record ErrorResponseDto(String apiPath, Integer errorStatus, String errorMessage, LocalDateTime errorTime) {
}
