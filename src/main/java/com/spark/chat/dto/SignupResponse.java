package com.example.signup.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignupResponse {
    private boolean success;
    private String field;
    private String message;
    private String token;
}
