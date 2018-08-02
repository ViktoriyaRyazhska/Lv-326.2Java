package com.softserve.edu.cajillo.util;

import com.softserve.edu.cajillo.dto.LoginRequestDto;

import java.nio.charset.Charset;
import java.util.Base64;

public class Base64DecoderUtils {
    public static LoginRequestDto decodeAuthorizationHeader(String header) {

        String base64Credentials = header.substring("Basic".length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                Charset.forName("UTF-8"));
        final String[] values = credentials.split(":", 2);
        return new LoginRequestDto(values[0], values[1]);
    }
}