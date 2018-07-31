package com.softserve.edu.cajillo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto extends BaseDto {

    private String username;

    private String firstName;

    private String lastName;

    private String email;
}