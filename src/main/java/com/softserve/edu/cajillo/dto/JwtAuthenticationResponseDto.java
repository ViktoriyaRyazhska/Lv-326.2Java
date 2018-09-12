package com.softserve.edu.cajillo.dto;

import com.softserve.edu.cajillo.entity.User;
import lombok.Data;

@Data
public class JwtAuthenticationResponseDto {

    private String accessToken;

    private String tokenType = "Bearer";

    private User.ChosenLanguage chosenLanguage;

    public JwtAuthenticationResponseDto(String accessToken, User.ChosenLanguage chosenLanguage) {
        this.accessToken = accessToken;
        this.chosenLanguage = chosenLanguage;
    }
}