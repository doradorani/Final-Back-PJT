package com.office.agijagi_back.Service.Interface;

import com.office.agijagi_back.Dto.UserDto;

public interface IUserService {

    public String getEmailByRefreshToken(String refreshToken);

    public int deleteRefreshTokenByToken(String refreshToken);

    public int deleteUser(String email);

    public UserDto info(String email);

    public int dupNickname(String userNickname);
}
