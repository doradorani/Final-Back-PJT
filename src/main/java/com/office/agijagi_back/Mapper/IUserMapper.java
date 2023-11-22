package com.office.agijagi_back.Mapper;

import com.office.agijagi_back.Dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IUserMapper {

    int deleteUser(String email);
    UserDto info(String email);
    int dupNickname(String userNickname);
    int modifyInfo(UserDto modifyUserDto);
    String getImgByEmail(String email);
    List<Map<String, Object>> userManageList(int perPage, int offset);
    int totalPageByUserManageList(int perPage);
    int totalCntByUserManageList();
    Map<String, Object> showUserDetailByEmail(String email);
}
