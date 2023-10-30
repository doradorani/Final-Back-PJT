package com.office.agijagi_back.Service.Interface;

import com.office.agijagi_back.Dto.AdminDto;

public interface IAdminService  {
    AdminDto signUp(AdminDto adminDto);
    int signIn (AdminDto adminDto);

    int logOut(String refreshToken);

    String getAdminIDByRefreshToken(String refreshToken);

    int signOut(String id);
}
