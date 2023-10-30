package com.office.agijagi_back.Service;

import com.office.agijagi_back.Mapper.IUserMapper;
import com.office.agijagi_back.Util.Jwt.IjwtMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final IUserMapper iUserMapper;
    private final IjwtMapper ijwtMapper;

    public UserService(IUserMapper iUserMapper, IjwtMapper ijwtMapper) {
        this.iUserMapper = iUserMapper;
        this.ijwtMapper = ijwtMapper;
    }


    public String getEmailByRefreshToken(String refreshToken) {

        return ijwtMapper.getEmailByRefreshToken(refreshToken);
    }

    public int deleteRefreshTokenByToken(String refreshToken) {

        return ijwtMapper.deleteRefreshTokenByToken(refreshToken);
    }

    public int deleteUser(String email) {

        return iUserMapper.deleteUser(email);
    }
}
