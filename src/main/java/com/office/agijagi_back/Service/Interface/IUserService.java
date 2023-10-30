package com.office.agijagi_back.Service.Interface;

public interface IUserService {

    public String getEmailByRefreshToken(String refreshToken);

    public int deleteRefreshTokenByToken(String refreshToken);

    public int deleteUser(String email);

}
