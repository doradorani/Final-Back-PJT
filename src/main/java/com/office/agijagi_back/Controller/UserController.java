package com.office.agijagi_back.Controller;

import com.office.agijagi_back.Dto.UserDto;
import com.office.agijagi_back.Service.ResponseService;
import com.office.agijagi_back.Service.UserService;
import com.office.agijagi_back.Util.Jwt.JwtProvider;
import com.office.agijagi_back.Util.Jwt.TokenDto;
import com.office.agijagi_back.Util.Jwt.TokenService;
import com.office.agijagi_back.Util.Response.SingleResult;
import com.office.agijagi_back.Util.S3.S3Service;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@Log4j2
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final TokenService tokenService;
    private final ResponseService responseService;
    private final S3Service s3Service;

    public UserController(UserService userService, JwtProvider jwtProvider, TokenService tokenService, ResponseService responseService, S3Service s3Service) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.tokenService = tokenService;
        this.responseService = responseService;
        this.s3Service = s3Service;
    }

    @ApiOperation(httpMethod = "POST"
            , value = "Access Token 검증 User.ver"
            , notes = "check access token validate for user"
            , response = UserDto.class
            , responseContainer = "SingleResult")
    @PostMapping("/validate")
    public SingleResult<UserDto> validate() {

        //SecurityContextHolder에서 정보 가져옴
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();

        log.info("현재 사용자의 userName: " + userName);

        UserDto dto = new UserDto(1, userName);

        return responseService.getSingleResult(dto);
    }
    @ApiOperation(httpMethod = "POST"
            , value = "새 토큰 발급 User.ver"
            , notes = "generate a new token"
            , response = TokenDto.class
            , responseContainer = "ResponseEntity")
    @PostMapping("/newToken")
    public ResponseEntity newToken(HttpServletRequest request) {

        String refreshToken = "";
        String email = "";
        Cookie[] list = request.getCookies();
        if (list != null) {
            for (Cookie cookie : list) {
                if (cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue().substring(6);
                }
            }
        }

        if(jwtProvider.validateToken(refreshToken)){
            email = userService.getEmailByRefreshToken(refreshToken);
        }

        return tokenService.setNewAccessToken(jwtProvider.newAccessToken(email, "ROLE_USER"));
    }
    @ApiOperation(httpMethod = "POST"
            , value = "로그아웃 User.ver"
            , notes = "log out"
            , response = void.class
            , responseContainer = "void")
    @PostMapping("/logOut")
    public void logOut(HttpServletRequest request, HttpServletResponse response){

        String refreshToken = "";
        Cookie[] list = request.getCookies();
        if (list != null) {
            for (Cookie cookie : list) {
                if (cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue().substring(6);
                }
            }
        }
        int deleteToken = userService.deleteRefreshTokenByToken(refreshToken);

        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

    }
    @ApiOperation(httpMethod = "POST"
            , value = "회원탈퇴 User.ver"
            , notes = "sign out"
            , response = int.class
            , responseContainer = "SingleResult")
    @PostMapping("/signOut")
    public int signOut(HttpServletRequest request, HttpServletResponse response){
        log.info("signOut()");

        String refreshToken = "";
        String email = "";

        Cookie[] list = request.getCookies();
        if (list != null) {
            for (Cookie cookie : list) {
                System.out.println(cookie.getName());
                if (cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue().substring(6);
                }
            }
        }
        else{
            System.out.println("없음");
        }

        email = userService.getEmailByRefreshToken(refreshToken);

        int deleteToken = userService.deleteRefreshTokenByToken(refreshToken);

        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return userService.deleteUser(email);
    }
    @ApiOperation(httpMethod = "GET"
            , value = "해당 계정 정보 조회 User.ver"
            , notes = "select my info for user"
            , response = UserDto.class
            , responseContainer = "SingleResult")
    @GetMapping("/info")
    public UserDto info(){
        log.info("info()");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        UserDto userDto = userService.info(email);

        return userDto;
    }
    @ApiOperation(httpMethod = "GET"
            , value = "별명 중복 조회 User.ver"
            , notes = "check nickname duplicated"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @GetMapping("/dupNickname/{userNickname}")
    public SingleResult<Integer> dupNickname(@PathVariable String userNickname) throws IOException {
        log.info("dupNickname()");

        int result = userService.dupNickname(userNickname);

        return responseService.getSingleResult(result);
    }

    @ApiOperation(httpMethod = "POST"
            , value = "내 정보 수정 User.ver"
            , notes = "update my info"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @PostMapping(value = "/modifyInfo", consumes = {"multipart/form-data"})
    public SingleResult<Integer> modifyInfo(@RequestPart(value = "file", required = false) MultipartFile file,
                                            @RequestPart("info") Map<String, Object> item) throws IOException {

        log.info("modifyInfo()");

        String imgUrl = null;
        if (file != null) {
            imgUrl = s3Service.uploadFile(file);
        }

        UserDto modifyUserDto = new UserDto(item.get("name"),
                item.get("nickname"),
                item.get("email"),
                item.get("phone"),
                item.get("zip_code"),
                item.get("address_detail1"),
                item.get("address_detail2"),
                imgUrl);

        return responseService.getSingleResult(userService.modifyInfo(modifyUserDto));
    }

}
