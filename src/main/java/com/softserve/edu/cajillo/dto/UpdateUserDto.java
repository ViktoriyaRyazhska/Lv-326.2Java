package com.softserve.edu.cajillo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto extends UserDto {

    private String oldPassword;

    private String newPassword;

    private String repeatPassword;
}