package com.office.agijagi_back.Util.Jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //resolveToken 메소드로 요청 헤더에서 토큰을 꺼내고,
        //JwtProvider의 validateToken 메소드로 토큰의 유효성을 검사
        //이때 유효한 토큰이면 토큰이 인증 정보를 뱉는데, 이걸 SecurityContext에 담아 필터링 로직을 진행
        String token = resolveToken(request);

        // 2. validateToken 으로 토큰 유효성 검사
        if (token != null && jwtProvider.validateToken(token)) {
            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
            Authentication authentication = jwtProvider.getAuthentication(token);

            System.out.println("권한 => " + authentication);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        else{
            System.out.println("인증 실패");
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }

        return null;
    }

}
