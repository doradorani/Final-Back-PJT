package com.office.agijagi_back.Service;

import com.office.agijagi_back.Dto.UserDto;
import com.office.agijagi_back.Mapper.IUserMapper;
import com.office.agijagi_back.Service.Interface.IUserService;
import com.office.agijagi_back.Util.Jwt.IjwtMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    private final IUserMapper userMapper;
    private final IjwtMapper jwtMapper;

    public UserService(IUserMapper userMapper, IjwtMapper jwtMapper) {
        this.userMapper = userMapper;
        this.jwtMapper = jwtMapper;
    }


    public String getEmailByRefreshToken(String refreshToken) {

        return jwtMapper.getEmailByRefreshToken(refreshToken);
    }

    public int deleteRefreshTokenByToken(String refreshToken) {

        return jwtMapper.deleteRefreshTokenByToken(refreshToken);
    }

    public int deleteUser(String email) {

        return userMapper.deleteUser(email);
    }

    @Override
    public UserDto info(String email) {

        return userMapper.info(email);
    }

    @Override
    public int dupNickname(String userNickname) {

        return userMapper.dupNickname(userNickname);
    }

}
