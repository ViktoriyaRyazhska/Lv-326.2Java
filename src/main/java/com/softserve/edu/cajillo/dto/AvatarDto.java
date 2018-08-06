package com.softserve.edu.cajillo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class AvatarDto extends BaseDto {
    @NotBlank
    private String avatar;
}
