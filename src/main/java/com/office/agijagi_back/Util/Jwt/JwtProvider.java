package com.office.agijagi_back.Util.Jwt;

import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Log4j2
@Component
public class JwtProvider {

    private final SecretKey key;

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24;  //20초
    //서버 지연 고려 (10초)
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;

    private final IjwtMapper ijwtMapper;

    public JwtProvider(@Value("${spring.jwt.secret}") String secretKey, IjwtMapper ijwtMapper) {
        this.ijwtMapper = ijwtMapper;
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    //email을 Claim으로 하여 JWT를 생성
    //Refresh Token은 로그인 유지를 위한 것이므로 Claim 없이 만료 시간만 담아줌
    public TokenDto generateTokenDto(String email, String role) {

        String accessToken = generateAccessToken(email, role);
        String refreshToken = generateRefreshToken();

        //기존에 있던 refreshToken이 있다면 삭제
        //다른 클라이언트에서 접속 시
        if (role == "ROLE_USER") {
            ijwtMapper.deleteRefreshTokenByEmail(email);
            ijwtMapper.insertRefreshToken(email, refreshToken);
        } else if (role == "ROLE_ADMIN") {
            ijwtMapper.deleteAdminRefreshTokenByEmail(email);
            ijwtMapper.insertAdminRefreshToken(email, refreshToken);
        }

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken("Bearer" + refreshToken)
                .build();
    }

    // Access Token 재발급
    public TokenDto newAccessToken(String email, String role) {
        String accessToken = generateAccessToken(email, role);
        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .build();
    }

    // Access Token 생성
    public String generateAccessToken(String email, String role) {
        long now = (new Date()).getTime();
        Claims claims = Jwts.claims().setSubject(email);
        claims.put(AUTHORITIES_KEY, new SimpleGrantedAuthority(role).getAuthority());

        String accessToken = Jwts.builder()                                     //이 빌더를 사용하여 JWT의 내용을 구성
                .setSubject(email)                                              //payload "sub" : "name" => "sub" 클레임은 토큰의 주체
                .setClaims(claims)                                              //payload "auth" : "256USER", "테스트" : "test"
                .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRE_TIME))        //payload "exp" : 1234567890 (10자리)
                .signWith(SignatureAlgorithm.HS256, key)                        //header "alg" : 해싱 알고리즘 HS256
                .setHeaderParam("typ", "JWT")                                   //header "typ" : JWT
                .compact();                                                     //JWT를 실제 문자열 형식으로 변환하여 반환합니다. 이 문자열은 생성된 JWT 토큰을 나타냄
        return accessToken;
    }

    // Refresh Token 생성
    public String generateRefreshToken() {
        long now = (new Date()).getTime();
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, key)
                .setHeaderParam("typ", "JWT")
                .compact();
        return refreshToken;
    }

    //엑세스 토큰의 권한 확인하기
    public Authentication getAuthentication(String accessToken) {

        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보를 추출하고 권한 객체(GrantedAuthority)의 컬렉션을 만듬
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    //엑세스 토큰을 파싱하고 클레임 정보를 반환합니다.
    //만약 토큰이 만료되었다면 (ExpiredJwtException 예외가 발생했다면), 만료된 클레임 정보를 반환합니다.
    //이것은 주어진 엑세스 토큰에서 권한 정보를 추출하고 해당 정보를 사용하여 인증 객체를 만드는 과정입니다.
    // 이 인증 객체는 Spring Security에서 사용자를 나타내고, 권한을 부여하는 데 중요한 역할을 합니다.
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser()
                    .setSigningKey(key) // 시크릿 키 설정
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public boolean validateToken(String token) {

        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }

        return false;
    }

}
