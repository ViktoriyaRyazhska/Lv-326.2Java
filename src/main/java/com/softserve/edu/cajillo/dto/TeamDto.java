package com.softserve.edu.cajillo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamDto extends BaseDto {

    private String name;

    private String description;

    private String avatar;
}