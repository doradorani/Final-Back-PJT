package com.office.agijagi_back.Mapper;

import com.office.agijagi_back.Dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ICommunityMapper {
    List<PostDto> selectAllPosts();

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
}
