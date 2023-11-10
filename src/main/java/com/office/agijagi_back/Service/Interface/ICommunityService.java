package com.office.agijagi_back.Service.Interface;

import com.office.agijagi_back.Dto.PostDto;

import java.util.List;

public interface ICommunityService {
    List<PostDto> getAllPosts();

    PostDto getDetailPost(int no);

    int uploadPost(List<String> imgUrls, String text, String user_mail);
}
