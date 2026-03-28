package com.thepathways.callee_service.exception;

import java.time.LocalDateTime;

/**
 * A Java Record is a concise way to create data-only classes.
 * It automatically handles constructor, getters, equals, and hashCode.
 */
public record ErrorResponse(
    String message, 
    LocalDateTime timestamp, 
    int status, 
    String error
) {}
