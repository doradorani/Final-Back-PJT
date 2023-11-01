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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

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

    @PostMapping("/home")
    public ResponseEntity home() {
        log.info("home()");

        return ResponseEntity.ok(true);
    }

    @PostMapping("/signUp")
    public SingleResult<Integer> signUp(@RequestBody Map<String, String> signInfo) {
        log.info("signUp()");

        AdminDto adminDto = new AdminDto(signInfo.get("adminAccount"),
                                        signInfo.get("password"),
                                        signInfo.get("name"),
                                        signInfo.get("email"),
                                        signInfo.get("phoneNumber"));

        SingleResult<Integer> result = responseService.getSingleResult(adminService.signUp(adminDto));

        return result;
    }

    @PostMapping("/signIn")
    public ResponseEntity signIn(@RequestBody Map<String, String> signInfo) {
        log.info("signIn()");

        AdminDto adminDto = new AdminDto(signInfo.get("adminAccount"),
                                        signInfo.get("password"));

        return adminService.signIn(adminDto);
    }

    @PostMapping("/newToken")
    public ResponseEntity newToken(HttpServletRequest request) {

        String refreshToken = "";
        String id = "";
        Cookie[] list = request.getCookies();
        if (list != null) {
            for (Cookie cookie : list) {
                if (cookie.getName().equals("refreshTokenAdmin")) {
                    refreshToken = cookie.getValue().substring(6);
                }
            }
        }

        if(jwtProvider.validateToken(refreshToken)){
            id = adminService.getAdminIDByRefreshToken(refreshToken);
        }

        return tokenService.setNewAccessToken(jwtProvider.newAccessToken(id, "ROLE_ADMIN"));
    }

    @PostMapping("/logOut")
    public void logOut(HttpServletRequest request, HttpServletResponse response){

        String refreshToken = "";
        Cookie[] list = request.getCookies();
        if (list != null) {
            for (Cookie cookie : list) {
                if (cookie.getName().equals("refreshTokenAdmin")) {
                    refreshToken = cookie.getValue().substring(6);
                }
            }
        }
        int deleteToken = adminService.logOut(refreshToken);

        Cookie cookie = new Cookie("refreshTokenAdmin", null);
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
                if (cookie.getName().equals("refreshTokenAdmin")) {
                    refreshToken = cookie.getValue().substring(6);
                }
            }
        }

        id = adminService.getAdminIDByRefreshToken(refreshToken);
        int delete = adminService.logOut(refreshToken);
        int deleteAdmin = adminService.signOut(id);

        Cookie cookie = new Cookie("refreshTokenAdmin", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

    }
}
