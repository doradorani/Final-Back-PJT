package com.office.agijagi_back.Util.Jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {

    private String grantType;   // Bearer
    private String accessToken;
    private String refreshToken;

    //관리자용
    private int adminGrade;

    //사용자용
    private int newUser;
    private String userName;
    private String userNickname;
    private String userEmail;
    private String img;
    private int userStatus;

    public TokenDto(int userStatus) {
        this.userStatus = userStatus;
    }
}