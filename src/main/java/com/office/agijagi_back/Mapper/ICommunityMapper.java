package com.office.agijagi_back.Mapper;

import com.office.agijagi_back.Dto.PostDto;
import com.office.agijagi_back.Util.Response.SingleResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ICommunityMapper {
    List<PostDto> selectAllPosts();

    PostDto selectDetailPost(int no);

    int insertNewPost(PostDto postDto);

    List<PostDto> selectMyPosts(String userEmail);
}
