package com.softserve.edu.cajillo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ResetPasswordDto {

    @NotBlank
    private Long userId;

    @NotBlank
    private String token;

    @NotBlank
    private String password;
}
