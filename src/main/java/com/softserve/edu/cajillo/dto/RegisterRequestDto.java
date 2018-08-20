package com.softserve.edu.cajillo.dto;

import lombok.Data;

@Data
public class RegisterRequestDto extends BaseDto {

    private String username;

    private String email;

    private String password;
}