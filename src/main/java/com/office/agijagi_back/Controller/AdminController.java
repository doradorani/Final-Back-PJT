package com.office.agijagi_back.Controller;

import com.office.agijagi_back.Dto.AdminDto;
import com.office.agijagi_back.Dto.UserDto;
import com.office.agijagi_back.Util.Jwt.JwtProvider;
import com.office.agijagi_back.Util.Jwt.TokenDto;
import com.office.agijagi_back.Util.Jwt.TokenService;
import com.office.agijagi_back.Util.Response.SingleResult;
import com.office.agijagi_back.Service.AdminService;
import com.office.agijagi_back.Service.ResponseService;
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
    @ApiOperation(httpMethod = "POST"
            , value = "Access Token 검증 Admin.ver"
            , notes = "check access token validate for admin"
            , response = Boolean.class
            , responseContainer = "ResponseEntity")
    @PostMapping("/validate")
    public ResponseEntity validate() {
        log.info("[AdminController] validate");

        return ResponseEntity.ok(true);
    }
    @ApiOperation(httpMethod = "POST"
            , value = "회원가입 Admin.ver"
            , notes = "sign up"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @PostMapping("/signUp")
    public SingleResult<Integer> signUp(@RequestBody Map<String, String> signInfo) {
        log.info("[AdminController] signUp");

        AdminDto adminDto = new AdminDto(signInfo.get("adminAccount"),
                                        signInfo.get("password"),
                                        signInfo.get("name"),
                                        signInfo.get("email"),
                                        signInfo.get("phoneNumber"));

        SingleResult<Integer> result = responseService.getSingleResult(adminService.signUp(adminDto));

        return result;
    }
    @ApiOperation(httpMethod = "POST"
            , value = "로그인 Admin.ver"
            , notes = "sign in"
            , response = AdminDto.class
            , responseContainer = "ResponseEntity")
    @PostMapping("/signIn")
    public ResponseEntity signIn(@RequestBody Map<String, String> signInfo) {
        log.info("[AdminController] signIn");

        AdminDto adminDto = new AdminDto(signInfo.get("adminAccount"),
                                        signInfo.get("password"));

        return adminService.signIn(adminDto);
    }
    @ApiOperation(httpMethod = "POST"
            , value = "새 토큰 발급 Admin.ver"
            , notes = "generate a new token"
            , response = TokenDto.class
            , responseContainer = "ResponseEntity")
    @PostMapping("/newToken")
    public ResponseEntity newToken(HttpServletRequest request) {
        log.info("[AdminController] newToken");

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
    @ApiOperation(httpMethod = "POST"
            , value = "로그아웃 Admin.ver"
            , notes = "log out"
            , response = void.class
            , responseContainer = "void")
    @PostMapping("/logOut")
    public void logOut(HttpServletRequest request, HttpServletResponse response){
        log.info("[AdminController] logOut");

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
    @ApiOperation(httpMethod = "POST"
            , value = "회원탈퇴 Admin.ver"
            , notes = "sign out"
            , response = void.class
            , responseContainer = "void")
    @PostMapping("/signOut")
    public void signOut(HttpServletRequest request, HttpServletResponse response){
        log.info("[AdminController] signOut");

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
    @ApiOperation(httpMethod = "GET"
            , value = "모든 사용자 조회 Admin.ver"
            , notes = "select all users"
            , response = Map.class
            , responseContainer = "SingleResult")
    @GetMapping("userManageList/{currentPage}/{perPage}")
    public SingleResult<Map> userManageList(@PathVariable @Valid int currentPage,
                                       @PathVariable @Valid int perPage) throws IOException {
        log.info("[AdminController] userManageList");

        return responseService.getSingleResult(adminService.userManageList(currentPage, perPage));
    }
    @ApiOperation(httpMethod = "GET"
            , value = "사용자 detail 조회 Admin.ver"
            , notes = "select user detail"
            , response = Map.class
            , responseContainer = "SingleResult")
    @GetMapping("showUserDetail/{email}")
    public SingleResult<Map> showUserDetail(@PathVariable @Valid String email) throws IOException {
        log.info("[AdminController] showUserDetail");

        return responseService.getSingleResult(adminService.showUserDetail(email));
    }
    @ApiOperation(httpMethod = "GET"
            , value = "승인된 모든 관리자 조회 for Super Admin Admin.ver"
            , notes = "select all authorized admins"
            , response = Map.class
            , responseContainer = "SingleResult")
    @GetMapping("authList/{currentPage}/{perPage}")
    public SingleResult<Map> authList(@PathVariable @Valid int currentPage,
                                            @PathVariable @Valid int perPage) throws IOException {
        log.info("[AdminController] authList");

        return responseService.getSingleResult(adminService.authList(currentPage, perPage));
    }
    @ApiOperation(httpMethod = "GET"
            , value = "미승인된 모든 관리자 조회 for Super Admin Admin.ver"
            , notes = "select all unauthorized admins"
            , response = Map.class
            , responseContainer = "SingleResult")
    @GetMapping("noneAuthList/{currentPage}/{perPage}")
    public SingleResult<Map> noneAuthList(@PathVariable @Valid int currentPage,
                                      @PathVariable @Valid int perPage) throws IOException {
        log.info("[AdminController] noneAuthList");

        return responseService.getSingleResult(adminService.noneAuthList(currentPage, perPage));
    }
    @ApiOperation(httpMethod = "PUT"
            , value = "관리자 권한 수정 Admin.ver"
            , notes = "update admin's grade"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @PutMapping("updateGrade/{no}/{gradeData}")
    public SingleResult<Integer> updateGrade(@PathVariable @Valid int no,
                                         @PathVariable @Valid int gradeData) throws IOException {
        log.info("[AdminController] updateGrade");

        return responseService.getSingleResult(adminService.updateGrade(no, gradeData));
    }
    @ApiOperation(httpMethod = "GET"
            , value = "해당 계정 정보 조회 Admin.ver"
            , notes = "select my info for admin"
            , response = Map.class
            , responseContainer = "SingleResult")
    @GetMapping("myAdminInfo/{adminAccount}")
    public SingleResult<Map> myAdminInfo(@PathVariable @Valid String adminAccount) throws IOException{
        log.info("[AdminController] myAdminInfo");

        return responseService.getSingleResult(adminService.myAdminInfo(adminAccount));
    }
    @ApiOperation(httpMethod = "PUT"
            , value = "해당 계정 정보 수정 Admin.ver"
            , notes = "update my info"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @PutMapping("modifyInfo")
    public SingleResult<Integer> modifyInfo(@RequestPart("info") Map<String, Object> item) throws IOException {
        log.info("[AdminController] updateAdminInfo");

        AdminDto modifyAdminDto = new AdminDto(item.get("id"),
                item.get("pw"),
                item.get("name"),
                item.get("email"),
                item.get("phone"),
                item.get("currentId"));

        return responseService.getSingleResult(adminService.modifyInfo(modifyAdminDto));
    }
    @ApiOperation(httpMethod = "PUT"
            , value = "사용자 권한 수정 Admin.ver"
            , notes = "update users status"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @PutMapping("/updateUserStatus/{no}/{statusData}")
    public SingleResult<Integer> updateUserStatus(@PathVariable int no,
                                                @PathVariable int statusData) throws IOException {
        log.info("[AdminController] updateUserStatus");

        return responseService.getSingleResult(adminService.updateUserStatus(no, statusData));
    }

}
