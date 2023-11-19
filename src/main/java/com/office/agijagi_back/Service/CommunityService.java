package com.office.agijagi_back.Service;

import com.office.agijagi_back.Dto.*;
import com.office.agijagi_back.Mapper.ICommunityMapper;
import com.office.agijagi_back.Mapper.IUserMapper;
import com.office.agijagi_back.Service.Interface.ICommunityService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

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

        return communityMapper.selectAllPosts();
    }
    @Override
    public List<PostDto> getMorePosts(int lastPostId) {
        log.info("[CommunityService] getMorePosts");

        return communityMapper.selectMorePosts(lastPostId);
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
    @Override
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
    @Override
    public int deletePost(int postIndex) {
        log.info("[CommunityService] deletePost");

        int result = 0;
        result = communityMapper.updatePostForDelete(postIndex);
        return result;
    }
    @Override
    public int updateEmotionBtn(int btnIndex, int post_no, String userMail) {
        log.info("[CommunityService] updateEmotionBtn");

        int result = 0;
        EmotionBtnDto emotionBtnDto = new EmotionBtnDto();
        emotionBtnDto.setUser_mail(userMail);
        emotionBtnDto.setPost_no(post_no);

        switch (btnIndex) {
            case 1:
                emotionBtnDto = communityMapper.selectLikeForUpdate(emotionBtnDto);
                result = communityMapper.updatePostForLike(emotionBtnDto);
                break;
            case 2:
//                result = communityMapper.updatePostForGreat(emotionBtnDto);
                break;
            case 3:
//                result = communityMapper.updatePostForSad(emotionBtnDto);
                break;
        }
        return result;

    }
    @Override
    public List<ReplyDto> getAllReplys(int postIndex) {
        log.info("[CommunityService] getAllReplys");

        return communityMapper.selectAllReplysByPostNo(postIndex);
    }
    @Override
    public int registReply(String user_mail,int postId,String  replyText) {
        log.info("[CommunityService] registReply");

        int result = communityMapper.updateReplyCnt(postId);
        if (result > 0) {
            ReplyDto replyDto = new ReplyDto();
            replyDto.setUser_mail(user_mail);
            replyDto.setPost_no(postId);
            replyDto.setComment(replyText);
            return communityMapper.insertNewReply(replyDto);
        }

        return result;
    }
    @Override
    public int registReReply(String user_mail, int postId, String replyText, int replyIndex) {
        log.info("[CommunityService] registReReply");

        int result = communityMapper.updateReplyCnt(postId);
        if (result > 0) {
            ReplyDto replyDto = new ReplyDto();
            replyDto.setUser_mail(user_mail);
            replyDto.setPost_no(postId);
            replyDto.setReply_no(replyIndex);
            replyDto.setComment(replyText);
            return communityMapper.insertNewReReply(replyDto);
        }

        return result;
    }
    @Override
    public int deleteReply(int postId, int replyIndex) {
        log.info("[CommunityService] deleteReply");

        int result = communityMapper.updateReplyCntForDelete(postId);
        int deleteResult = 0;
        if (result > 0) {
            deleteResult = communityMapper.updateReplyForDelete(replyIndex);
        }

        return deleteResult;
    }
    @Override
    public int modifyReply(int replyIndex, String replyText) {
        log.info("[CommunityService] modifyReply");

        ReplyDto replyDto  = new ReplyDto();
        replyDto.setNo(replyIndex);
        replyDto.setComment(replyText);

        int result =communityMapper.updateReplyForModify(replyDto);

        return result;
    }
    @Override
    public int summitReport(int postId, int replyIndex, String reportReason, String user_mail) {
        log.info("[CommunityService] modifyReply");

        if (replyIndex == 0) {
            PostReportDto postReportDto = new PostReportDto();
            postReportDto.setPost_no(postId);
            postReportDto.setReason(reportReason);
            postReportDto.setReport_user(user_mail);
            return communityMapper.insertPostReport(postReportDto);

        } else {
            ReplyReportDto replyReportDto = new ReplyReportDto();
            replyReportDto.setReply_no(replyIndex);
            replyReportDto.setReason(reportReason);
            replyReportDto.setReport_user(user_mail);
            return communityMapper.insertReplyReport(replyReportDto);
        }
    }
}
