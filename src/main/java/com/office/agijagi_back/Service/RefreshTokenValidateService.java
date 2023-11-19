package com.office.agijagi_back.Service;

import com.office.agijagi_back.Service.Interface.IRefreshTokenValidateService;
import com.office.agijagi_back.Util.Jwt.IjwtMapper;
import com.office.agijagi_back.Util.Jwt.JwtProvider;
import com.office.agijagi_back.Util.Response.ErrorCode;
import com.office.agijagi_back.Util.Response.ErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Service
@Log4j2
public class RefreshTokenValidateService implements IRefreshTokenValidateService {

    private final JwtProvider jwtProvider;
    private final IjwtMapper jwtMapper;

    public RefreshTokenValidateService(JwtProvider jwtProvider, IjwtMapper jwtMapper) {
        this.jwtProvider = jwtProvider;
        this.jwtMapper = jwtMapper;
    }

    @Override
    public String refreshTokenValidate(HttpServletRequest request) {
        log.info("[RefreshTokenValidateService] refreshTokenValidate");
        String refreshToken = "";
        Cookie[] list = request.getCookies();
        if (list != null) {
            for (Cookie cookie : list) {
                if (cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue().substring(6);
                }
            }
        }

        if(jwtProvider.validateToken(refreshToken)){
            return jwtMapper.getEmailByRefreshToken(refreshToken);
        } else {
            throw null;
        }

    }
}
