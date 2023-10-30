package com.office.agijagi_back.Util.Kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


@Log4j2
@Controller
@RequestMapping("/kakao")
public class KakaoController {

    private final KakaoService kakaoService ;

    public KakaoController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @PostMapping("/login")
    public ResponseEntity kakaoLogin(@RequestParam ("code") String code, HttpServletResponse response) throws JsonProcessingException {
        // code: 카카오 서버로부터 받은 인가 코드

        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        KakaoTokenDto kakaoTokenDto = new KakaoTokenDto();

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // POST 요청을 위해 기본값이 false인 256DoOutput을 true로 설정
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // POST 요청에 필요로 하는 파라미터를 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=675a168b3fa5bd60254c92260c6a62d4"); // REST_API_KEY를 입력
            sb.append("&redirect_uri=http://localhost:3000/login/oauth2/callback/kakao"); // 인가 코드를 받은 redirect_uri를 입력
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            // 결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();

            // 요청을 통해 얻은 JSON 타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(result);

            access_Token = jsonNode.get("access_token").asText();
            //refresh_Token = jsonNode.get("refresh_token").asText();

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //엑세스 토큰 이용해서 사용자 정보 가져오기
        kakaoTokenDto = getUserInfo(access_Token);

        return kakaoService.login(kakaoTokenDto);
    }


    private KakaoTokenDto getUserInfo(String accessToken) {

        String reqURL = "https://kapi.kakao.com/v2/user/me";
        KakaoTokenDto kakaoTokenDto = new KakaoTokenDto();

        // access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken); // 전송할 header 작성, access_token 전송

            // 결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();

            // 요청을 통해 얻은 JSON 타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            // Jackson ObjectMapper를 사용하여 JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(result);

            int id = jsonNode.get("id").asInt();
            JsonNode kakaoAccount = jsonNode.get("kakao_account");
            boolean hasEmail = kakaoAccount.get("has_email").asBoolean();
            String userName = "";
            String email = "";
            if (hasEmail) {
                userName = kakaoAccount.get("profile").get("nickname").asText();
                email = kakaoAccount.get("email").asText();
            }

            kakaoTokenDto.setUserName(userName);
            kakaoTokenDto.setEmail(email);

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return kakaoTokenDto;
    }


}
