package com.office.agijagi_back.Util.Kakao;

import com.office.agijagi_back.Util.Jwt.JwtProvider;
import com.office.agijagi_back.Util.Jwt.TokenDto;
import com.office.agijagi_back.Util.Jwt.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class KakaoService {

    private final IKakaoMapper kakaoMapper;
    private final JwtProvider jwtProvider;
    private final TokenService tokenService;

    public KakaoService(IKakaoMapper kakaoMapper, JwtProvider jwtProvider, TokenService tokenService) {
        this.kakaoMapper = kakaoMapper;
        this.jwtProvider = jwtProvider;
        this.tokenService = tokenService;
    }

    public ResponseEntity login(KakaoTokenDto kakaoTokenDto) {

        int isUser = kakaoMapper.isUser(kakaoTokenDto.getEmail());

        int createUser = 0;

        if(isUser <= 0){        //회원가입
            createUser = kakaoMapper.insertUser(kakaoTokenDto);
        }

        TokenDto tokenDto = jwtProvider.generateTokenDto(kakaoTokenDto.getEmail(), "ROLE_USER");
        tokenDto.setNewUser(createUser);

        return tokenService.setTokenForFront(tokenDto);

    }
}
