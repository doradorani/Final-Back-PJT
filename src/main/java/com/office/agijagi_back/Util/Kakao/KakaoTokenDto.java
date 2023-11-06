package com.office.agijagi_back.Util.Kakao;


import lombok.Data;

@Data
public class KakaoTokenDto {
    private String no;
    private String userName;
    private String userNickname;
    private String email;
    private String refreshToken;
    private String newUser;
}
