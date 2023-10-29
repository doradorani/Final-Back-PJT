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

    public int logOut(String refreshToken) {

        return ijwtMapper.logOut(refreshToken);
    }

    public int signOut(String email) {

        return iUserMapper.signOut(email);
    }
}
