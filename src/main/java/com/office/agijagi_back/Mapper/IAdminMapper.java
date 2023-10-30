package com.office.agijagi_back.Mapper;

import com.office.agijagi_back.Dto.AdminDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IAdminMapper {

    AdminDto insertAdmin(AdminDto adminDto);
    int selectAdmin(AdminDto adminDto);
    int deleteAdminRefreshToken(String refreshToken);
    int updateAdminToWithdraw(String id);

    String selectAdminIDByRefreshToken(String refreshToken);
}
