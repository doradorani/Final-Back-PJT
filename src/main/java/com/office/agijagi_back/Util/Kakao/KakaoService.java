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
        String newName = "";
        String newEmail = "";

        if(isUser <= 0){        //회원가입
            String randomNickname = generateRandomNickname(10);
            kakaoTokenDto.setUserNickname(randomNickname);
            createUser = kakaoMapper.insertUser(kakaoTokenDto);
            newName = kakaoTokenDto.getUserName();
            newEmail = kakaoTokenDto.getEmail();
        }
        else{   //이미 존재하는 유저
            //제제
            int ban = kakaoMapper.getStatusByEmail(kakaoTokenDto.getEmail());
            if(ban == 2){
                TokenDto banDto = new TokenDto(ban);
                return ResponseEntity.ok().body(banDto);
            }

            //탈퇴했던 유저 복귀
            int returnUser = kakaoMapper.returnUser(kakaoTokenDto.getEmail());  //정보는있는데 상태가 0이면
            if(returnUser == 0){
                int comeBackSuccess = kakaoMapper.comeBackUser(kakaoTokenDto.getEmail());
                if(comeBackSuccess > 0){
                    createUser = 2;
                    newName = kakaoTokenDto.getUserName();
                    newEmail = kakaoTokenDto.getEmail();
                }
            }
        }

        String nickname = kakaoMapper.getLoginNicknameByEmail(kakaoTokenDto.getEmail());
        String img = kakaoMapper.getLoginProfileByEmail(kakaoTokenDto.getEmail());

        TokenDto tokenDto = jwtProvider.generateTokenDto(kakaoTokenDto.getEmail(), "ROLE_USER");
        tokenDto.setNewUser(createUser);
        tokenDto.setUserNickname(nickname);
        tokenDto.setImg(img);
        tokenDto.setUserName(newName);
        tokenDto.setUserEmail(newEmail);

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
