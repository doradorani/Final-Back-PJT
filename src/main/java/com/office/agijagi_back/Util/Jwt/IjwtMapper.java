package com.office.agijagi_back.Util.Jwt;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IjwtMapper {

    void insertRefreshToken(String email, String refreshToken);
    String getEmailByRefreshToken(String refreshToken);
    int deleteRefreshTokenByToken(String refreshToken);
    int deleteRefreshTokenByEmail(String email);
    int insertAdminRefreshToken(String email, String refreshToken);
    int deleteAdminRefreshTokenByEmail(String email);

}
