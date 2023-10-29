package com.office.agijagi_back.Util.Jwt;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    //비정상적인 JWT를 가지고 접근 시 401 UNAUTHORIZED 응답
    //JWT 토큰이 없거나 유효하지 않은 경우
    //만료기간이 끝났을 경우

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //401

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);


    }

}
