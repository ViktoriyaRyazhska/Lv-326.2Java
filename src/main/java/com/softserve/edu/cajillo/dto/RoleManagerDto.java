package com.softserve.edu.cajillo.dto;

import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.entity.Team;
import com.softserve.edu.cajillo.entity.User;
import com.softserve.edu.cajillo.entity.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleManagerDto extends BaseDto {

    private Board board;

    private User user;

    private RoleName roleName;

    private Team team;
}