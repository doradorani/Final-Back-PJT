package com.office.agijagi_back.Mapper;

import com.office.agijagi_back.Dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ICommunityMapper {
    List<PostDto> selectAllPosts(String user_mail);

    PostDto selectDetailPost(int no);

    int insertNewPost(PostDto postDto);

    List<PostDto> selectMyPosts(String userEmail);

    List<PostDto> selectMorePosts(int lastPostId);

    int updatePostForDelete(int postIndex);

    int updatePostForLike(EmotionBtnDto emotionBtnDto);

    List<ReplyDto> selectAllReplysByPostNo(int postIndex);

    int insertNewReply(ReplyDto replyDto);

    int updateReplyCnt(int postId);

    int insertNewReReply(ReplyDto replyDto);

    int updateReplyCntForDelete(int postId);

    int updateReplyForDelete(int replyIndex);

    int updateReplyForModify(ReplyDto replyDto);

    int insertPostReport(PostReportDto postReportDto);

    int insertReplyReport(ReplyReportDto replyReportDto);

    int selectCurrnetReplyNo();

    int insertLikeByPostNo(EmotionBtnDto emotionBtnDto);

    Integer selectBtnNoByPostNo(EmotionBtnDto emotionBtnDto);

    int selectBtnNoExistsForPostNo(EmotionBtnDto emotionBtnDto);

    int updatePostForGreat(EmotionBtnDto emotionBtnDto);

    int updatePostForSad(EmotionBtnDto emotionBtnDto);

    int updatePostForLikeDelete(EmotionBtnDto emotionBtnDto);

    int updatePostForGreatDelete(EmotionBtnDto emotionBtnDto);

    int updatePostForSadDelete(EmotionBtnDto emotionBtnDto);

    int deleteLikeByPostNo(EmotionBtnDto emotionBtnDto);

    int updateLikeByPostNo(EmotionBtnDto emotionBtnDto);

    List<PostDto> selectMyLikedPosts(String userMail);
}
