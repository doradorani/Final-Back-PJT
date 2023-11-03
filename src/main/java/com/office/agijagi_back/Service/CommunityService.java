package com.office.agijagi_back.Service;

import com.office.agijagi_back.Dto.PostDto;
import com.office.agijagi_back.Mapper.ICommunityMapper;
import com.office.agijagi_back.Service.Interface.ICommunityService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class CommunityService implements ICommunityService {

    private final ICommunityMapper communityMapper;

    public CommunityService(ICommunityMapper communityMapper){
        this.communityMapper = communityMapper;
    }

    @Override
    public List<PostDto> getAllPosts() {
        log.info("[CommunityService] getAllPosts");

        return communityMapper.selectAllPosts();
    }
    @Override
    public PostDto getDetailPost(int no) {
        log.info("[CommunityService] getDetailPost");

        return communityMapper.selectDetailPost(no);
    }

    @Override
    public int uploadPost(PostDto postDto) {
        log.info("[CommunityService] uploadPost");

        return communityMapper.insertNewPost(postDto);
    }

    public List<PostDto> getMyPosts(String userEmail) {
        log.info("[CommunityService] getMyPosts");

        return communityMapper.selectMyPosts(userEmail);
    }
}
