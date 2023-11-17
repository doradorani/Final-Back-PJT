package com.office.agijagi_back.Util.Jwt;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class TokenService {

    public ResponseEntity<TokenDto> setTokenForFront(String tokenName, TokenDto tokenDto){

        ResponseCookie cookie = ResponseCookie.from(tokenName, tokenDto.getRefreshToken())
                .maxAge(7 * 24 * 60 * 60)
                .path("/")
//                .secure(true) // HTTP에서도 사용 가능
//                .sameSite("None") // 모든 도메인 및 포트에서 사용
                .httpOnly(true) // JavaScript에서 접근 불가
                .build();

        // 쿠키를 응답 헤더에 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

        tokenDto.setRefreshToken("");

        return ResponseEntity.ok()
                .headers(headers)  // 응답 헤더에 쿠키를 추가
                .body(tokenDto);
    }

    public ResponseEntity<TokenDto> setNewAccessToken(TokenDto tokenDto){
        log.info("New AccessToken");
        log.info(tokenDto.getAccessToken());
        log.info(tokenDto.getRefreshToken());
        return ResponseEntity.ok()
                .body(tokenDto);
    }
}

