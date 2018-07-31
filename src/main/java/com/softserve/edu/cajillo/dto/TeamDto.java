package com.softserve.edu.cajillo.dto;

import com.softserve.edu.cajillo.entity.RoleManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamDto extends BaseDto {

    private String name;

    private String description;

    private String avatar;

    private List<RoleManager> roleManagers;
}