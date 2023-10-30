package com.office.agijagi_back.Controller;

import com.office.agijagi_back.Dto.AdminDto;
import com.office.agijagi_back.Util.Jwt.JwtProvider;
import com.office.agijagi_back.Util.Jwt.TokenService;
import com.office.agijagi_back.Util.Response.SingleResult;
import com.office.agijagi_back.Service.AdminService;
import com.office.agijagi_back.Service.ResponseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Log4j2
@RequestMapping("/admin")
public class AdminController {

    private AdminService adminService;
    private ResponseService responseService;
    private TokenService tokenService;
    private JwtProvider jwtProvider;

    public AdminController(AdminService adminService, ResponseService responseService, TokenService tokenService, JwtProvider jwtProvider){
        this.adminService = adminService;
        this.responseService = responseService;
        this.tokenService = tokenService;
        this.jwtProvider = jwtProvider;
    }


    @PostMapping("/signUp")
    public SingleResult<AdminDto> signUp(AdminDto adminDto) {
        log.info("AdminController(signUp)");

         return responseService.getSingleResult(adminService.signUp(adminDto));
    }

    @PostMapping("/signIn")
    public SingleResult<Integer> signIn(AdminDto adminDto) {
        log.info("AdminController(signIn)");

        return responseService.getSingleResult(adminService.signIn(adminDto));
    }

    @PostMapping("/newToken")
    public ResponseEntity newToken(HttpServletRequest request) {

        String refreshToken = "";
        String id = "";
        Cookie[] list = request.getCookies();
        if (list != null) {
            for (Cookie cookie : list) {
                if (cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue().substring(6);
                }
            }
        }

        if(jwtProvider.validateToken(refreshToken)){
            id = adminService.getAdminIDByRefreshToken(refreshToken);
        }

        // admin에서는 id를 식별값으로 사용해서 jwtProvider에서 새 accesstoken 발급 메소드를 새로 만들어야 할 듯

//        return tokenService.setNewAccessToken(jwtProvider.newAccessToken(id, "ROLE_ADMIN"));
        return null;
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
        int deleteToken = adminService.logOut(refreshToken);

        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @PostMapping("/signOut")
    public void signOut(HttpServletRequest request, HttpServletResponse response){

        String refreshToken = "";
        String id = "";
        Cookie[] list = request.getCookies();

        if (list != null) {
            for (Cookie cookie : list) {
                if (cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue().substring(6);
                }
            }
        }

        id = adminService.getAdminIDByRefreshToken(refreshToken);
        int delete = adminService.logOut(refreshToken);
        int deleteAdmin = adminService.signOut(id);

        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

    }
}
