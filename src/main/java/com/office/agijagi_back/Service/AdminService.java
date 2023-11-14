package com.office.agijagi_back.Service;

import com.office.agijagi_back.Dto.AdminDto;
import com.office.agijagi_back.Dto.UserDto;
import com.office.agijagi_back.Mapper.IAdminMapper;
import com.office.agijagi_back.Mapper.IUserMapper;
import com.office.agijagi_back.Service.Interface.IAdminService;
import com.office.agijagi_back.Util.Jwt.IjwtMapper;
import com.office.agijagi_back.Util.Jwt.JwtProvider;
import com.office.agijagi_back.Util.Jwt.TokenDto;
import com.office.agijagi_back.Util.Jwt.TokenService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class AdminService implements IAdminService {

    private final IAdminMapper adminMapper;
    private final IUserMapper userMapper;

    private final JwtProvider jwtProvider;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AdminService(IAdminMapper adminMapper, JwtProvider jwtProvider, IjwtMapper jwtMapper, IUserMapper userMapper, TokenService tokenService, PasswordEncoder passwordEncoder){
        this.adminMapper = adminMapper;
        this.jwtProvider = jwtProvider;
        this.userMapper = userMapper;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public int signUp(AdminDto adminDto) {
        log.info("signUp()");

        AdminDto isAdmin = adminMapper.signInAdminById(adminDto.getId());

        if(isAdmin != null) {
            return 0;
        }
        adminDto.setPw(passwordEncoder.encode(adminDto.getPw()));

        return adminMapper.insertAdmin(adminDto);
    }

    @Override
    public ResponseEntity signIn (AdminDto adminDto){
        log.info("signIn()");

        AdminDto loginedAdminDto = adminMapper.signInAdminById(adminDto.getId());

        if(loginedAdminDto != null) {
            if (passwordEncoder.matches(adminDto.getPw(), loginedAdminDto.getPw())) {

                TokenDto tokenDto = jwtProvider.generateTokenDto(adminDto.getId(), "ROLE_ADMIN");
                tokenDto.setAdminGrade(loginedAdminDto.getGrade());

                return tokenService.setTokenForFront("refreshTokenAdmin", tokenDto);
            }
        }

        return ResponseEntity.badRequest().body(false);
    }
    @Override
    public int logOut(String refreshToken){
        log.info("AdminService(logOut)");

        return adminMapper.deleteAdminRefreshToken(refreshToken);
    }

    @Override
    public String getAdminIDByRefreshToken(String refreshToken) {
        log.info("AdminService(getEmailByRefreshToken)");

        return adminMapper.selectAdminIDByRefreshToken(refreshToken);
    }

    @Override
    public int signOut(String id) {
        log.info("AdminService(signOut)");

        return adminMapper.updateAdminToWithdraw(id);
    }

    @Override
    public Map<String, Object> userManageList(int currentPage, int perPage) {
        log.info("userManageList()");

        int offset = (currentPage - 1) * perPage;

        List<Map<String, Object>> userManageListDtos = userMapper.userManageList(perPage, offset);
        int totalPages = userMapper.totalPageByUserManageList(perPage);
        int userManageListCnt = userMapper.totalCntByUserManageList();

        Map<String, Object> userListMap = new HashMap<>();
        userListMap.put("userManageListDtos", userManageListDtos);
        userListMap.put("totalPages", totalPages);
        userListMap.put("userManageListCnt", userManageListCnt);

        return userListMap;
    }

    @Override
    public Map<String, Object> authList(int currentPage, int perPage) {
        log.info("authList()");

        int offset = (currentPage - 1) * perPage;

        List<Map<String, Object>> authListDtos = adminMapper.authList(perPage, offset);
        int totalPages = adminMapper.totalPageByauthList(perPage);
        int userManageListCnt = adminMapper.totalCntByauthList();

        Map<String, Object> authListMap = new HashMap<>();
        authListMap.put("authListDtos", authListDtos);
        authListMap.put("totalPages", totalPages);
        authListMap.put("userManageListCnt", userManageListCnt);

        return authListMap;
    }

    @Override
    public Map<String, Object> noneAuthList(int currentPage, int perPage) {
        log.info("noneAuthList()");

        int offset = (currentPage - 1) * perPage;

        List<Map<String, Object>> noneAuthListDtos = adminMapper.noneAuthList(perPage, offset);
        int totalPages = adminMapper.totalPageBynoneAuthList(perPage);
        int userManageListCnt = adminMapper.totalCntBynoneAuthList();

        Map<String, Object> noneAuthListMap = new HashMap<>();
        noneAuthListMap.put("noneAuthListDtos", noneAuthListDtos);
        noneAuthListMap.put("totalPages", totalPages);
        noneAuthListMap.put("userManageListCnt", userManageListCnt);

        return noneAuthListMap;
    }

    @Override
    public int updateGrade(int no, int gradeData) {
        log.info("updateGrade()");

        return adminMapper.updateGradeByNoAndGrade(no, gradeData);
    }

    @Override
    public Map<String, Object> showUserDetail(String email) {
        log.info("showUserDetail()");

        return userMapper.showUserDetailByEmail(email);
    }

    @Override
    public Map<String, Object> myAdminInfo(String adminAccount) {
        log.info("myAdminInfo()");

        return adminMapper.myAdminInfoById(adminAccount);
    }

    @Override
    public int modifyInfo(AdminDto modifyAdminDto) {
        log.info("modifyInfo()");

        //아이디 변경 시
        if(!modifyAdminDto.getCurrentId().equals(modifyAdminDto.getId())){

            //변경한 아이디가 이미 존재한다면
            int dupCnt = adminMapper.duplicateById(modifyAdminDto.getId());
            if(dupCnt > 0){
                return 0;
            }
        }

        if(modifyAdminDto.getPw() != null && modifyAdminDto.getPw() != ""){
            modifyAdminDto.setPw(passwordEncoder.encode(modifyAdminDto.getPw()));
            log.info("modifyAdminInfoAndPw()");
            return adminMapper.modifyAdminInfoAndPw(modifyAdminDto);
        }
        else{
            log.info("modifyAdminInfo()");
            return adminMapper.modifyAdminInfo(modifyAdminDto);
        }
    }
}
