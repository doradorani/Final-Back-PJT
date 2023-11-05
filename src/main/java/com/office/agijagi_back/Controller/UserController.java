package com.office.agijagi_back.Controller;

import com.office.agijagi_back.Dto.UserDto;
import com.office.agijagi_back.Service.ResponseService;
import com.office.agijagi_back.Service.UserService;
import com.office.agijagi_back.Util.Jwt.JwtProvider;
import com.office.agijagi_back.Util.Jwt.TokenService;
import com.office.agijagi_back.Util.Response.SingleResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Map;

@RestController
@Log4j2
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final TokenService tokenService;
    private final ResponseService responseService;

    public UserController(UserService userService, JwtProvider jwtProvider, TokenService tokenService, ResponseService responseService) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.tokenService = tokenService;
        this.responseService = responseService;
    }


    @PostMapping("/validate")
    public UserDto validate() {

        //SecurityContextHolder에서 정보 가져옴
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();

        System.out.println("현재 사용자의 userName: " + userName);

        UserDto dto = new UserDto(1, userName);

        return dto;
    }

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

    @PostMapping("/signOut")
    public void signOut(HttpServletRequest request, HttpServletResponse response){

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

        email = userService.getEmailByRefreshToken(refreshToken);
        int deleteToken = userService.deleteRefreshTokenByToken(refreshToken);
        int deleteUser = userService.deleteUser(email);

        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @GetMapping("/info")
    public UserDto info(){
        log.info("info()");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        UserDto userDto = userService.info(email);
        return userDto;
    }

    @GetMapping("/dupNickname/{userNickname}")
    public SingleResult<Integer> dupNickname(@PathVariable String userNickname) {
        log.info("dupNickname()");

        int result = userService.dupNickname(userNickname);
        log.info("result{}", result);


        return responseService.getSingleResult(result);
    }

//    @PostMapping(value ="/modifyInfo")
//    public SingleResult<Integer> modifyInfo(HttpServletRequest request, @RequestPart("info") Map<String, Object> item, String fileName) {
//
//        System.out.println(file);
//        System.out.println(item);
//        System.out.println(fileName);
//
//        return null;
//    }


    @PostMapping(value = "/modifyInfo", consumes = {"multipart/form-data"})
    public SingleResult<Integer> modifyInfo(@RequestPart("file") MultipartFile file,
                                            @RequestPart("info") Map<String, Object> item) {
        // 파일 및 정보를 처리하는 로직 작성
        // file 변수에는 업로드된 파일이, item 변수에는 JSON 데이터가, fileName 변수에는 파일 이름이 전달됩니다.

        System.out.println("file : " + file.getOriginalFilename());
        System.out.println("item : " + item);

        return null;
    }

}
