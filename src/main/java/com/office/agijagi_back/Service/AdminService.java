package com.office.agijagi_back.Service;

import com.office.agijagi_back.Dto.AdminDto;
import com.office.agijagi_back.Mapper.IAdminMapper;
import com.office.agijagi_back.Service.Interface.IAdminService;
import com.office.agijagi_back.Util.Jwt.IjwtMapper;
import com.office.agijagi_back.Util.Jwt.JwtProvider;
import com.office.agijagi_back.Util.Jwt.TokenService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class AdminService implements IAdminService {

    private final IAdminMapper adminMapper;

    private final JwtProvider jwtProvider;
    private final TokenService tokenService;

    public AdminService(IAdminMapper adminMapper, JwtProvider jwtProvider, IjwtMapper jwtMapper, TokenService tokenService){
        this.adminMapper = adminMapper;
        this.jwtProvider = jwtProvider;
        this.tokenService = tokenService;
    }


    @Override
    public AdminDto signUp(AdminDto adminDto) {
        log.info("AdminService(signUp)");


        return adminMapper.insertAdmin(adminDto);
    }

    @Override
    public int signIn (AdminDto adminDto){
        log.info("AdminService(signIn)");

        return adminMapper.selectAdmin(adminDto);
    }
    @Override
    public int logOut(String refreshToken){
        log.info("AdminService(logOut)");

        return adminMapper.deleteAdminRefreshToken(refreshToken);
    }

    @Override
    public String getAdminIDByRefreshToken(String refreshToken) {
        log.info("AdminService(getEmailByRefreshToken)");

        return adminMapper.selectAdminIDByRefreshToken(refreshToken);
    }

    @Override
    public int signOut(String id) {
        log.info("AdminService(signOut)");

        return adminMapper.updateAdminToWithdraw(id);
    }


}
