package com.office.agijagi_back.Mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserMapper {

    int deleteUser(String email);
}
