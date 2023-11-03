package com.office.agijagi_back.Controller;

import com.office.agijagi_back.Dto.PostDto;
import com.office.agijagi_back.Service.CommunityService;
import com.office.agijagi_back.Service.RefreshTokenValidateService;
import com.office.agijagi_back.Service.ResponseService;
import com.office.agijagi_back.Util.Response.SingleResult;
import com.office.agijagi_back.Util.S3.S3Service;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
    public List<PostDto> getAllPosts() {
        log.info("[CommunityController] getAllPosts");

        return communityService.getAllPosts();
    }

    @GetMapping("/getDetailPost")
    public PostDto getDetailPost(int no) {
        log.info("[CommunityController] getDetailPost");

        return communityService.getDetailPost(no);
    }

    @GetMapping("/uploadPost")
    public SingleResult<Integer> uploadPost(@RequestPart(value = "data", required = false) Map<String, String> data,
                                            @RequestPart(value = "file", required = false) MultipartFile file ,
                                            HttpServletRequest request)  throws IOException {
        log.info("[CommunityController] uploadPost");

        String user_email = refreshTokenValidateService.refreshTokenValidate(request);

        if (data != null) {
            String imgUrl = s3Service.uploadFile(file);
            PostDto postDto = new PostDto(0, user_email, data.get("text_content"), imgUrl, 0, 0, 0, 0, null, null);

            return responseService.getSingleResult(communityService.uploadPost(postDto));
        }
        return responseService.getSingleResult(0);
    }

//    @GetMapping("/myPosts")
//    public getListResult<PostDto> getMyPosts(HttpServletRequest request) throws IOException {
//        log.info("[CommunityController] getMyPosts");
//
//        String user_email = refreshTokenValidateService.refreshTokenValidate(request);
//
//        if(user_email != null) {
//            return responseService.getListResult(communityService.getMyPosts(user_email));
//        }
//        return responseService.getListResult(null);
//    }

}
