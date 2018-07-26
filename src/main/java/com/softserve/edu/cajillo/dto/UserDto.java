package com.softserve.edu.cajillo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
}
