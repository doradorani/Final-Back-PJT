package com.office.agijagi_back.Service;

import com.office.agijagi_back.Dto.AdminDto;
import com.office.agijagi_back.Mapper.IAdminMapper;
import com.office.agijagi_back.Service.Interface.IAdminService;
import com.office.agijagi_back.Util.Jwt.IjwtMapper;
import com.office.agijagi_back.Util.Jwt.JwtProvider;
import com.office.agijagi_back.Util.Jwt.TokenDto;
import com.office.agijagi_back.Util.Jwt.TokenService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
public class AdminService implements IAdminService {

    private final IAdminMapper adminMapper;

    private final JwtProvider jwtProvider;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AdminService(IAdminMapper adminMapper, JwtProvider jwtProvider, IjwtMapper jwtMapper, TokenService tokenService, PasswordEncoder passwordEncoder){
        this.adminMapper = adminMapper;
        this.jwtProvider = jwtProvider;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public int signUp(AdminDto adminDto) {
        log.info("signUp()");

        AdminDto isAdmin = adminMapper.signInAdminById(adminDto.getId());

        if(isAdmin != null) {
            return 0;
        }
        adminDto.setPw(passwordEncoder.encode(adminDto.getPw()));

        return adminMapper.insertAdmin(adminDto);
    }

    @Override
    public ResponseEntity signIn (AdminDto adminDto){
        log.info("signIn()");

        AdminDto loginedAdminDto = adminMapper.signInAdminById(adminDto.getId());

        if(loginedAdminDto != null) {
            if (passwordEncoder.matches(adminDto.getPw(), loginedAdminDto.getPw())) {

                TokenDto tokenDto = jwtProvider.generateTokenDto(adminDto.getId(), "ROLE_ADMIN");

                return tokenService.setTokenForFront("refreshTokenAdmin", tokenDto);
            }
        }

        return ResponseEntity.badRequest().body(false);
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
