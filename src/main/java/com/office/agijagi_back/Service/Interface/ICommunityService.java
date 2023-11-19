package com.office.agijagi_back.Service.Interface;

import com.office.agijagi_back.Dto.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ICommunityService {
    List<PostDto> getAllPosts(String user_mail);
    List<PostDto> getMorePosts(int lastPostId);
    PostDto getDetailPost(int postId);
    int uploadPost(List<String> imgUrls, String text, String user_mail);
    Map<String, Object> getMyPosts(String email);
    int deletePost(int postIndex);
    int updateEmotionBtn(int btnIndex, int post_no, String userMail);
    List<ReplyDto> getAllReplys(int postIndex);
    int registReply(String user_mail,int postId,String  replyText);
    int registReReply(String user_mail, int postId, String replyText, int replyIndex);
    int deleteReply(int postId, int replyIndex);
    int modifyReply(int replyIndex, String replyText);
    int summitReport(int postId, int replyIndex, String reportReason, String user_mail);
    int getEmotions(int postNo, String userMail);
    Map<String, Object> getMyLikedPosts(String userMail);
}
