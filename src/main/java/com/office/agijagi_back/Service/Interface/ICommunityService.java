package com.office.agijagi_back.Service.Interface;

import com.office.agijagi_back.Dto.PostDto;

import java.util.List;

public interface ICommunityService {
    List<PostDto> getAllPosts();

    PostDto getDetailPost(int no);

    int uploadPost(PostDto postDto);
}
