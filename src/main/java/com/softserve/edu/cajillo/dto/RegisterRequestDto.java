package com.softserve.edu.cajillo.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class RegisterRequestDto extends BaseDto {

    @Size(min = 3)
    private String username;

    @Email
    private String email;

    @Size(min = 8)
    private String password;
}