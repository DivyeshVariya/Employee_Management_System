package com.info.ems.dtos.response;

import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.*;

/**
 * Response DTO that wraps response details such as status, status code, message, and data.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Response {
  
    private HttpStatus status;    // Status of the response
    private String message;       // Message detailing the response
    private Map<String, Object> data; // Data related to the response
    private int statusCode;

}
