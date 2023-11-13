package com.office.agijagi_back.Controller;

import com.office.agijagi_back.Dto.PostDto;
import com.office.agijagi_back.Dto.ReplyDto;
import com.office.agijagi_back.Service.CommunityService;
import com.office.agijagi_back.Service.RefreshTokenValidateService;
import com.office.agijagi_back.Service.ResponseService;
import com.office.agijagi_back.Util.Response.ListResult;
import com.office.agijagi_back.Util.Response.SingleResult;
import com.office.agijagi_back.Util.S3.S3Service;
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

    @GetMapping("/getAllPosts")
    public ListResult<PostDto> getAllPosts() {
        log.info("[CommunityController] getAllPosts");

        return responseService.getListResult(communityService.getAllPosts());
    }

    @GetMapping("/getMorePosts/{lastPostId}")
    public ListResult<PostDto> getMorePosts(@PathVariable @Valid int lastPostId) {
        log.info("[CommunityController] getMorePosts");

        return responseService.getListResult(communityService.getMorePosts(lastPostId));
    }

    @GetMapping("/getDetailPost/{postId}")
    public SingleResult<PostDto> getDetailPost(@PathVariable @Valid int postId) {
        log.info("[CommunityController] getDetailPost");

        return responseService.getSingleResult(communityService.getDetailPost(postId));
    }

    @GetMapping("/getMyPosts")
    public SingleResult<Map<String, Object>> getMyPosts() {
        log.info("[CommunityController] getMyPosts");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_mail = userDetails.getUsername();

        return responseService.getSingleResult(communityService.getMyPosts(user_mail));
    }

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

    @DeleteMapping("/deletePost/{postIndex}")
    public SingleResult<Integer> deletePost(@PathVariable @Valid int postIndex) {
        log.info("[CommunityController] deletePost");

        if (postIndex != 0) {
            return responseService.getSingleResult(communityService.deletePost(postIndex));
        } else {
            return responseService.getSingleResult(0);
        }
    }

    @PutMapping("/updateEmotionBtn/{btnIndex}/{postIndex}")
    public SingleResult<Integer> updateEmotionBtn(@PathVariable @Valid int btnIndex, @PathVariable @Valid int postIndex) {
        log.info("[CommunityController] deletePost");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_mail = userDetails.getUsername();

        return responseService.getSingleResult(communityService.updateEmotionBtn(btnIndex, postIndex, user_mail));
    }

    @GetMapping("/getReplys/{postIndex}")
    public ListResult<ReplyDto> getAllReplys(@PathVariable @Valid int postIndex) {
        log.info("[CommunityController] getAllPosts");

        return responseService.getListResult(communityService.getAllReplys(postIndex));
    }

    @PutMapping("/registReply/{postId}/{replyText}")
    public SingleResult<Integer> registReply(@PathVariable @Valid int postId, @PathVariable @Valid String replyText) {
        log.info("[CommunityController] registReply");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_mail = userDetails.getUsername();

        return responseService.getSingleResult(communityService.registReply(user_mail, postId, replyText));
    }

    @PutMapping("/registReReply/{postId}/{replyText}/{replyIndex}")
    public SingleResult<Integer> registReReply(@PathVariable @Valid int postId, @PathVariable @Valid String replyText, @PathVariable @Valid int replyIndex) {
        log.info("[CommunityController] registReReply");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_mail = userDetails.getUsername();

        return responseService.getSingleResult(communityService.registReReply(user_mail, postId, replyText, replyIndex));
    }

    @DeleteMapping("/deleteReply/{postId}/{replyIndex}")
    public SingleResult<Integer> deleteReply(@PathVariable @Valid int postId, @PathVariable @Valid int replyIndex) {
        log.info("[CommunityController] deletePost");

        if (replyIndex != 0) {
            return responseService.getSingleResult(communityService.deleteReply(postId, replyIndex));
        } else {
            return responseService.getSingleResult(0);
        }
    }

    @PutMapping("/modifyReply/{replyIndex}/{replyText}")
    public SingleResult<Integer> modifyReply(@PathVariable @Valid int replyIndex, @PathVariable @Valid String replyText) {
        log.info("[CommunityController] modifyReply");

        if (replyIndex != 0) {
            return responseService.getSingleResult(communityService.modifyReply(replyIndex, replyText));
        } else {
            return responseService.getSingleResult(0);
        }
    }

    @PostMapping("/summitReport/{postId}/{replyIndex}/{reportReason}")
    public SingleResult<Integer> summitReport (@PathVariable @Valid int postId, @PathVariable @Valid int replyIndex, @PathVariable @Valid String reportReason) {
        log.info("[CommunityController] summitReport");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user_mail = userDetails.getUsername();

        return responseService.getSingleResult(communityService.summitReport(postId, replyIndex, reportReason, user_mail));
    }
}
