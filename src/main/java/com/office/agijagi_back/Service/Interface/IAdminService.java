package com.office.agijagi_back.Service.Interface;

import com.office.agijagi_back.Dto.AdminDto;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface IAdminService  {

    int signUp(AdminDto adminDto);
    ResponseEntity signIn (AdminDto adminDto);
    int logOut(String refreshToken);
    String getAdminIDByRefreshToken(String refreshToken);
    int signOut(String id);
    Map<String, Object> userManageList(int currentPage, int perPage);
    Map<String, Object> authList(int currentPage, int perPage);
    Map<String, Object> noneAuthList(int currentPage, int perPage);
    int updateGrade(int no, int gradeData);
    Map<String, Object> showUserDetail(String email);
    Map<String, Object> myAdminInfo(String adminAccount);
    int modifyInfo(AdminDto modifyAdminDto);
}
