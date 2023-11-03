package com.office.agijagi_back.Service.Interface;

import com.office.agijagi_back.Dto.AdminDto;
import org.springframework.http.ResponseEntity;

public interface IAdminService  {

    int signUp(AdminDto adminDto);

    ResponseEntity signIn (AdminDto adminDto);

    int logOut(String refreshToken);

    String getAdminIDByRefreshToken(String refreshToken);

    int signOut(String id);



}
