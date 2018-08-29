package com.softserve.edu.cajillo.dto;

import com.softserve.edu.cajillo.entity.enums.BoardType;
import lombok.Data;

@Data
public class SimpleBoardDto extends BaseDto {
    private Long id;

    private String name;

    private String image;

    private BoardType boardType;
}
