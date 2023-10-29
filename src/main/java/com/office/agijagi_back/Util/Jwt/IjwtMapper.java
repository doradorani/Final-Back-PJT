package com.office.agijagi_back.Util.Jwt;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IjwtMapper {

    void setToken(String email, String refreshToken);
    String getEmailByRefreshToken(String refreshToken);
    int logOut(String refreshToken);

}
