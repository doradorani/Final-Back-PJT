package com.office.agijagi_back.Util.Kakao;

import com.office.agijagi_back.Dto.UserDto;
import com.office.agijagi_back.Util.Jwt.JwtProvider;
import com.office.agijagi_back.Util.Jwt.TokenDto;
import com.office.agijagi_back.Util.Jwt.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Random;

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
            String randomNickname = generateRandomNickname(10);
            kakaoTokenDto.setUserNickname(randomNickname);
            createUser = kakaoMapper.insertUser(kakaoTokenDto);
        }

        String nickname = kakaoMapper.getLoginNicknameByEmail(kakaoTokenDto.getEmail());
        String img = kakaoMapper.getLoginProfileByEmail(kakaoTokenDto.getEmail());

        TokenDto tokenDto = jwtProvider.generateTokenDto(kakaoTokenDto.getEmail(), "ROLE_USER");
        tokenDto.setNewUser(createUser);
        tokenDto.setUserNickname(nickname);
        tokenDto.setImg(img);

        return tokenService.setTokenForFront("refreshToken", tokenDto);
    }

    public static String generateRandomNickname(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder nickname = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            nickname.append(characters.charAt(index));
        }

        return nickname.toString();
    }
}
