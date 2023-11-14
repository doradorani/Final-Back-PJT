package com.office.agijagi_back.Controller;

import com.office.agijagi_back.Dto.AdminDto;
import com.office.agijagi_back.Dto.UserDto;
import com.office.agijagi_back.Util.Jwt.JwtProvider;
import com.office.agijagi_back.Util.Jwt.TokenService;
import com.office.agijagi_back.Util.Response.SingleResult;
import com.office.agijagi_back.Service.AdminService;
import com.office.agijagi_back.Service.ResponseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
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

    @PostMapping("/validate")
    public ResponseEntity validate() {
        log.info("validate()");

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

    @GetMapping("userManageList/{currentPage}/{perPage}")
    public SingleResult<Map> userManageList(@PathVariable @Valid int currentPage,
                                       @PathVariable @Valid int perPage) throws IOException {
        log.info("userManageList()");

        return responseService.getSingleResult(adminService.userManageList(currentPage, perPage));
    }

    @GetMapping("showUserDetail/{email}")
    public SingleResult<Map> showUserDetail(@PathVariable @Valid String email) throws IOException {
        log.info("showUserDetail()");

        return responseService.getSingleResult(adminService.showUserDetail(email));
    }

    @GetMapping("authList/{currentPage}/{perPage}")
    public SingleResult<Map> authList(@PathVariable @Valid int currentPage,
                                            @PathVariable @Valid int perPage) throws IOException {
        log.info("authList()");

        return responseService.getSingleResult(adminService.authList(currentPage, perPage));
    }

    @GetMapping("noneAuthList/{currentPage}/{perPage}")
    public SingleResult<Map> noneAuthList(@PathVariable @Valid int currentPage,
                                      @PathVariable @Valid int perPage) throws IOException {
        log.info("noneAuthList()");

        return responseService.getSingleResult(adminService.noneAuthList(currentPage, perPage));
    }

    @PutMapping("updateGrade/{no}/{gradeData}")
    public SingleResult<Integer> updateGrade(@PathVariable @Valid int no,
                                         @PathVariable @Valid int gradeData) throws IOException {
        log.info("updateGrade()");

        return responseService.getSingleResult(adminService.updateGrade(no, gradeData));
    }

    @GetMapping("myAdminInfo/{adminAccount}")
    public SingleResult<Map> myAdminInfo(@PathVariable @Valid String adminAccount) throws IOException{
        log.info("myAdminInfo()");

        return responseService.getSingleResult(adminService.myAdminInfo(adminAccount));
    }

    @PutMapping("modifyInfo")
    public SingleResult<Integer> modifyInfo(@RequestPart("info") Map<String, Object> item) throws IOException {
        log.info("updateAdminInfo");

        AdminDto modifyAdminDto = new AdminDto(item.get("id"),
                item.get("pw"),
                item.get("name"),
                item.get("email"),
                item.get("phone"),
                item.get("currentId"));

        return responseService.getSingleResult(adminService.modifyInfo(modifyAdminDto));
    }

    @PutMapping("/updateUserStatus/{no}/{statusData}")
    public SingleResult<Integer> updateUserStatus(@PathVariable int no,
                                                @PathVariable int statusData) throws IOException {
        log.info("updateUserStatus()");

        return responseService.getSingleResult(adminService.updateUserStatus(no, statusData));
    }

}
