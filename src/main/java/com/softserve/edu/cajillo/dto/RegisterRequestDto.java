package com.softserve.edu.cajillo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RegisterRequestDto extends BaseDto {

    @NotBlank
    private String username;

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
