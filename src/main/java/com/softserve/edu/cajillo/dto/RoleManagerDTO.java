package com.softserve.edu.cajillo.dto;

import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.entity.Role;
import com.softserve.edu.cajillo.entity.Team;
import com.softserve.edu.cajillo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleManagerDTO extends BaseDto {

    private Board board;
    private User user;
    private Role role;
    private Team team;
}
