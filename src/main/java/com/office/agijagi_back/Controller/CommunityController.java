package com.office.agijagi_back.Controller;

import com.office.agijagi_back.Dto.PostDto;
import com.office.agijagi_back.Dto.ReplyDto;
import com.office.agijagi_back.Service.CommunityService;
import com.office.agijagi_back.Service.RefreshTokenValidateService;
import com.office.agijagi_back.Service.ResponseService;
import com.office.agijagi_back.Util.Response.ListResult;
import com.office.agijagi_back.Util.Response.SingleResult;
import com.office.agijagi_back.Util.S3.S3Service;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Log4j2
@RestController
@RequestMapping("/community")
public class CommunityController {
    private final ResponseService responseService;
    private final RefreshTokenValidateService refreshTokenValidateService;
    private final CommunityService communityService;
    private final S3Service s3Service;

    public CommunityController(ResponseService responseService, RefreshTokenValidateService refreshTokenValidateService, CommunityService communityService, S3Service s3Service) {
        this.refreshTokenValidateService = refreshTokenValidateService;
        this.responseService = responseService;
        this.communityService = communityService;
        this.s3Service = s3Service;
    }
    @ApiOperation(httpMethod = "GET"
            , value = "모든 게시물 조회"
            , notes = "select all posts"
            , response = PostDto.class
            , responseContainer = "ListResult")
    @GetMapping("/getAllPosts")
    public ListResult<PostDto> getAllPosts() {
        log.info("[CommunityController] getAllPosts");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_mail = userDetails.getUsername();

        return responseService.getListResult(communityService.getAllPosts(user_mail));
    }
    @ApiOperation(httpMethod = "GET"
            , value = "모든 게시물 조회"
            , notes = "select all posts"
            , response = PostDto.class
            , responseContainer = "ListResult")
    @GetMapping("/getMorePosts/{lastPostId}")
    public ListResult<PostDto> getMorePosts(@PathVariable @Valid int lastPostId) {
        log.info("[CommunityController] getMorePosts");

        return responseService.getListResult(communityService.getMorePosts(lastPostId));
    }
    @ApiOperation(httpMethod = "GET"
            , value = "해당 게시물 상세 조회"
            , notes = "select post detail"
            , response = PostDto.class
            , responseContainer = "SingleResult")
    @GetMapping("/getDetailPost/{postId}")
    public SingleResult<PostDto> getDetailPost(@PathVariable @Valid int postId) {
        log.info("[CommunityController] getDetailPost");

        return responseService.getSingleResult(communityService.getDetailPost(postId));
    }
    @ApiOperation(httpMethod = "GET"
            , value = "해당 계정의 모든 게시물 조회"
            , notes = "select all my posts"
            , response = Map.class
            , responseContainer = "SingleResult")
    @GetMapping("/getMyPosts")
    public SingleResult<Map<String, Object>> getMyPosts() {
        log.info("[CommunityController] getMyPosts");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_mail = userDetails.getUsername();

        return responseService.getSingleResult(communityService.getMyPosts(user_mail));
    }
    @ApiOperation(httpMethod = "POST"
            , value = "게시물 작성"
            , notes = "insert post"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @PostMapping("/uploadPost")
    public SingleResult<Integer> uploadPost(@RequestPart(value = "data") @ApiParam(value = "data", required = true) Map<String, String> data,
                                            @RequestPart(value = "files") @ApiParam(value = "files", required = true) List<MultipartFile> files ,
                                            HttpServletRequest request)  throws IOException {
        log.info("[CommunityController] uploadPost");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_mail = userDetails.getUsername();

        if (data != null) {
            List<String> imgUrls = s3Service.uploadListFiles(files);

            return responseService.getSingleResult(communityService.uploadPost(imgUrls, data.get("text"), user_mail));
        }
        return responseService.getSingleResult(0);
    }
    @ApiOperation(httpMethod = "DELETE"
            , value = "게시물 삭제"
            , notes = "delete post"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @DeleteMapping("/deletePost/{postIndex}")
    public SingleResult<Integer> deletePost(@PathVariable @Valid int postIndex) {
        log.info("[CommunityController] deletePost");

        if (postIndex != 0) {
            return responseService.getSingleResult(communityService.deletePost(postIndex));
        } else {
            return responseService.getSingleResult(0);
        }
    }
    @ApiOperation(httpMethod = "PUT"
            , value = "해당 게시물 공감 버튼 수정"
            , notes = "delete post"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @PutMapping("/updateEmotionBtn/{btnIndex}/{post_no}")
    public SingleResult<Integer> updateEmotionBtn(@PathVariable @Valid int btnIndex, @PathVariable @Valid int post_no) {
        log.info("[CommunityController] updateEmotionBtn");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_mail = userDetails.getUsername();

        return responseService.getSingleResult(communityService.updateEmotionBtn(btnIndex, post_no, user_mail));
    }
    @ApiOperation(httpMethod = "GET"
            , value = "해당 게시물 모든 댓글 조회"
            , notes = "select all replies in a post"
            , response = ReplyDto.class
            , responseContainer = "ListResult")
    @GetMapping("/getReplys/{postIndex}")
    public ListResult<ReplyDto> getAllReplys(@PathVariable @Valid int postIndex) {
        log.info("[CommunityController] getAllReplys");

        return responseService.getListResult(communityService.getAllReplys(postIndex));
    }

    @ApiOperation(httpMethod = "POST"
            , value = "댓글 등록"
            , notes = "insert a reply in a post"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @PostMapping("/registReply/{postId}/{replyText}")
    public SingleResult<Integer> registReply(@PathVariable @Valid int postId, @PathVariable @Valid String replyText) {
        log.info("[CommunityController] registReply");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_mail = userDetails.getUsername();

        return responseService.getSingleResult(communityService.registReply(user_mail, postId, replyText));
    }
    @ApiOperation(httpMethod = "POST"
            , value = "대댓글 등록"
            , notes = "insert a re-reply in a post"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @PostMapping("/registReReply/{postId}/{replyText}/{replyIndex}")
    public SingleResult<Integer> registReReply(@PathVariable @Valid int postId, @PathVariable @Valid String replyText, @PathVariable @Valid int replyIndex) {
        log.info("[CommunityController] registReReply");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_mail = userDetails.getUsername();

        return responseService.getSingleResult(communityService.registReReply(user_mail, postId, replyText, replyIndex));
    }

    @ApiOperation(httpMethod = "DELETE"
            , value = "댓글 삭제"
            , notes = "delete reply"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @DeleteMapping("/deleteReply/{postId}/{replyIndex}")
    public SingleResult<Integer> deleteReply(@PathVariable @Valid int postId, @PathVariable @Valid int replyIndex) {
        log.info("[CommunityController] deleteReply");

        if (replyIndex != 0) {
            return responseService.getSingleResult(communityService.deleteReply(postId, replyIndex));
        } else {
            return responseService.getSingleResult(0);
        }
    }

    @ApiOperation(httpMethod = "PUT"
            , value = "댓글 수정"
            , notes = "update reply to modified version"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @PutMapping("/modifyReply/{replyIndex}/{replyText}")
    public SingleResult<Integer> modifyReply(@PathVariable @Valid int replyIndex, @PathVariable @Valid String replyText) {
        log.info("[CommunityController] modifyReply");

        if (replyIndex != 0) {
            return responseService.getSingleResult(communityService.modifyReply(replyIndex, replyText));
        } else {
            return responseService.getSingleResult(0);
        }
    }
    @ApiOperation(httpMethod = "POST"
            , value = "게시물 신고 등록"
            , notes = "insert post report"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @PostMapping("/summitReport/{postId}/{replyIndex}/{reportReason}")
    public SingleResult<Integer> summitReport (@PathVariable @Valid int postId, @PathVariable @Valid int replyIndex, @PathVariable @Valid String reportReason) {
        log.info("[CommunityController] summitReport");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_mail = userDetails.getUsername();

        return responseService.getSingleResult(communityService.summitReport(postId, replyIndex, reportReason, user_mail));
    }

    @ApiOperation(httpMethod = "GET"
            , value = "게시물 감정 버튼 조회"
            , notes = "get post emotions"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @GetMapping("/getEmotions/{post_no}")
    public SingleResult<Integer> getEmotions (@PathVariable @Valid int post_no) {
        log.info("[CommunityController] getEmotions");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_mail = userDetails.getUsername();

        return responseService.getSingleResult(communityService.getEmotions(post_no, user_mail));
    }

    @ApiOperation(httpMethod = "GET"
            , value = "해당 계정이 좋아요를 누른 게시물 조회"
            , notes = "select all my liked posts"
            , response = Map.class
            , responseContainer = "SingleResult")
    @GetMapping("/getMyLikedPosts")
    public SingleResult<Map<String, Object>> getMyLikedPosts() {
        log.info("[CommunityController] getMyLikedPosts");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_mail = userDetails.getUsername();

        return responseService.getSingleResult(communityService.getMyLikedPosts(user_mail));
    }
}
