package com.example.signup.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class SigninRequest {
    public String mobile;
    public String password;
    public boolean remember;
}
