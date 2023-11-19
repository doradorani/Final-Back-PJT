package com.office.agijagi_back.Service;

import com.office.agijagi_back.Dto.*;
import com.office.agijagi_back.Mapper.ICommunityMapper;
import com.office.agijagi_back.Mapper.IUserMapper;
import com.office.agijagi_back.Service.Interface.ICommunityService;
import lombok.extern.log4j.Log4j2;
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
    public List<PostDto> getAllPosts(String user_mail) {
        log.info("[CommunityService] getAllPosts");

        return communityMapper.selectAllPosts(user_mail);
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
        List<EmotionBtnDto> emotionBtnDtos = new ArrayList<>();

        int isBtnNo = communityMapper.selectBtnNoExistsForPostNo(emotionBtnDto);
        if (isBtnNo != 0) {
            Integer btnNo = communityMapper.selectBtnNoByPostNo(emotionBtnDto);

            if (btnNo == null) {
                return result;
            }

            if (btnNo == btnIndex) {
                // btnNo가 있고, 두 값이 같으면 좋아요 삭제
                int deleteResult = communityMapper.deleteLikeByPostNo(emotionBtnDto);
                if (btnIndex==1) {
                    int updateResult = communityMapper.updatePostForLikeDelete(emotionBtnDto);
                } else if (btnIndex==2) {
                    int updateResult = communityMapper.updatePostForGreatDelete(emotionBtnDto);
                } else if (btnIndex==3) {
                    int updateResult = communityMapper.updatePostForSadDelete(emotionBtnDto);
                }

            } else if (btnNo != btnIndex) {
                // btnNo가 있지만, 두 값이 다르면 좋아요 버튼 수정
                emotionBtnDto.setBtn_no(btnIndex);
                int modifyResult = communityMapper.updateLikeByPostNo(emotionBtnDto);
                if (btnNo == 1) {
                    int updateResult = communityMapper.updatePostForLikeDelete(emotionBtnDto);
                } else if (btnNo == 2) {
                    int updateResult = communityMapper.updatePostForGreatDelete(emotionBtnDto);
                } else if (btnNo == 3) {
                    int updateResult = communityMapper.updatePostForSadDelete(emotionBtnDto);
                }
                if (btnIndex==1) {
                    int updateResult = communityMapper.updatePostForLike(emotionBtnDto);
                } else if (btnIndex==2) {
                    int updateResult = communityMapper.updatePostForGreat(emotionBtnDto);
                } else if (btnIndex==3) {
                    int updateResult = communityMapper.updatePostForSad(emotionBtnDto);
                }
            }
        } else if (isBtnNo == 0) {
            // btnNo가 없으면 좋아요 등록
            emotionBtnDto.setBtn_no(btnIndex);
            int insertResult = communityMapper.insertLikeByPostNo(emotionBtnDto);
            if (btnIndex==1) {
                int updateResult = communityMapper.updatePostForLike(emotionBtnDto);
            } else if (btnIndex==2) {
                int updateResult = communityMapper.updatePostForGreat(emotionBtnDto);
            } else if (btnIndex==3) {
                int updateResult = communityMapper.updatePostForSad(emotionBtnDto);
            }

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
            int reply_no = communityMapper.selectCurrnetReplyNo();
            ReplyDto replyDto = new ReplyDto();
            replyDto.setReply_no(reply_no+1);
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

    public Integer getEmotions(int postNo, String userMail) {
        log.info("[CommunityService] getEmotions");

        EmotionBtnDto emotionBtnDto = new EmotionBtnDto();
        emotionBtnDto.setPost_no(postNo);
        emotionBtnDto.setUser_mail(userMail);
        Integer btnNo = communityMapper.selectBtnNoByPostNo(emotionBtnDto);

        return btnNo;
    }

    public Map<String, Object> getMyLikedPosts(String userMail) {
        log.info("[CommunityService] getMyLikedPosts");

        Map<String, Object> MyLikedPosts = new HashMap<>();
        UserDto userDto = userMapper.info(userMail);
        List<PostDto> postDtos = communityMapper.selectMyLikedPosts(userMail);
        MyLikedPosts.put("userDto", userDto);
        MyLikedPosts.put("postDtos", postDtos);

        return MyLikedPosts;
    }
}
