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
}
