package com.spark.chat.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ForgotPasswordRequest {
    public String mobile;
    public String otp;
    public String newPassword;
}
