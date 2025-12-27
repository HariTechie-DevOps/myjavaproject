package com.example.signup.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    private String name;
    private int age;
    private String gender;
    private String mobile;
    private String password;
}
