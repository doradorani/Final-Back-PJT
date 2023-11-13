package com.office.agijagi_back.Mapper;

import com.office.agijagi_back.Dto.AdminDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IAdminMapper {

    int insertAdmin(AdminDto adminDto);
    int selectAdmin(AdminDto adminDto);
    int deleteAdminRefreshToken(String refreshToken);
    int updateAdminToWithdraw(String id);
    String selectAdminIDByRefreshToken(String refreshToken);
    AdminDto signInAdminById(String id);
    List<Map<String, Object>> authList(int perPage, int offset);
    int totalPageByauthList(int perPage);
    int totalCntByauthList();
    List<Map<String, Object>> noneAuthList(int perPage, int offset);
    int totalPageBynoneAuthList(int perPage);
    int totalCntBynoneAuthList();
}
