package com.office.agijagi_back.Util.Kakao;


import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IKakaoMapper {

    int isUser(String email);
    int newUser(KakaoTokenDto kakaoTokenDto);


}
