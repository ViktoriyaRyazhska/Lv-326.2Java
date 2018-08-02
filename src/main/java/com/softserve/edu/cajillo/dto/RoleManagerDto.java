package com.softserve.edu.cajillo.dto;

import com.softserve.edu.cajillo.entity.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleManagerDto extends BaseDto {

    private Long board_id;

    private Long user_id;

    private RoleName roleName;

    private Long team_id;
}