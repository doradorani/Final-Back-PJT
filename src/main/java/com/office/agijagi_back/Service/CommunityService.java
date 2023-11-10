package com.office.agijagi_back.Service;

import com.office.agijagi_back.Dto.PostDto;
import com.office.agijagi_back.Dto.UserDto;
import com.office.agijagi_back.Mapper.ICommunityMapper;
import com.office.agijagi_back.Mapper.IUserMapper;
import com.office.agijagi_back.Service.Interface.ICommunityService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class CommunityService implements ICommunityService {

    private final ICommunityMapper communityMapper;
    private final IUserMapper userMapper;

    public CommunityService(ICommunityMapper communityMapper, IUserMapper userMapper){
        this.communityMapper = communityMapper;
        this.userMapper = userMapper;
    }

    @Override
    public List<PostDto> getAllPosts() {
        log.info("[CommunityService] getAllPosts");

        List<PostDto> postDtos = new ArrayList<>();
        postDtos = communityMapper.selectAllPosts();
        log.info("postDtos{}", postDtos);
        return communityMapper.selectAllPosts();
    }
    @Override
    public PostDto getDetailPost(int postId) {
        log.info("[CommunityService] getDetailPost");

        PostDto postdto = new PostDto();
        postdto = communityMapper.selectDetailPost(postId);
        log.info("postdto{}", postdto);

        return communityMapper.selectDetailPost(postId);
    }

    @Override
    public int uploadPost(List<String> imgUrls, String text, String user_mail) {
        log.info("[CommunityService] uploadPost");

        int result = 0;
        String imgUrl = "";
        for(int i = 0 ; i< imgUrls.size(); i++) {
            if (i == 0) {
                imgUrl = imgUrls.get(i);
            } else {
                imgUrl = imgUrl + "," + imgUrls.get(i);
            }
        }
        PostDto postDto = new PostDto();
        postDto.setImgs_path(imgUrl);
        postDto.setPost_text(text);
        postDto.setUser_mail(user_mail);
        result = communityMapper.insertNewPost(postDto);

        return result;
    }

    public Map<String, Object> getMyPosts(String email) {
        log.info("[CommunityService] getMyPosts");

        Map<String, Object> MyPosts = new HashMap<>();
        UserDto userDto = userMapper.info(email);
        List<PostDto> postDtos = communityMapper.selectMyPosts(email);
        log.info(userDto);
        log.info(postDtos);
        MyPosts.put("userDto", userDto);
        MyPosts.put("postDtos", postDtos);

        return MyPosts;
    }
}
