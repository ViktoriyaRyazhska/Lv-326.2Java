package com.softserve.edu.cajillo.dto;

import com.softserve.edu.cajillo.entity.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationDto extends BaseDto {
    private Long boardId;
    private Long userId;
    private RoleName roleName;
    private Long teamId;
}