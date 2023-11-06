package com.office.agijagi_back.Util.Jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class TokenDto {

    private String grantType;   // Bearer
    private String accessToken;
    private String refreshToken;

    private int newUser;
    private String userNickname;
    private String img;
}