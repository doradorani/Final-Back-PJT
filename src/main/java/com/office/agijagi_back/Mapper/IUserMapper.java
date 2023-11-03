package com.office.agijagi_back.Mapper;

import com.office.agijagi_back.Dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserMapper {

    int deleteUser(String email);
    UserDto info(String email);
    int dupNickname(String userNickname);
}
