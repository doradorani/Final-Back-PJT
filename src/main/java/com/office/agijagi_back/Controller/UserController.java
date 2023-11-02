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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public SingleResult<UserDto> validate() {
        log.info("[UserController] validate");

        //SecurityContextHolder에서 정보 가져옴
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();

        log.info("현재 사용자의 userName:{} ",userName);

        //
        UserDto dto = new UserDto("test", userName);

        return responseService.getSingleResult(dto);
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
            log.info("사용자 이메일 {}", email);
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


}
