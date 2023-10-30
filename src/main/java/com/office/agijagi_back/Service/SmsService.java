package com.office.agijagi_back.Service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.office.agijagi_back.Util.SMS.SmsDto;
import com.office.agijagi_back.Util.SMS.SmsRequestDto;
import com.office.agijagi_back.Util.SMS.SmsResponseDto;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class SmsService {

    @Value("${naver-cloud-sms.accessKey}")
    private String accessKey;

    @Value("${naver-cloud-sms.secretKey}")
    private String secretKey;

    @Value("${naver-cloud-sms.serviceId}")
    private String serviceId;

    @Value("${naver-cloud-sms.senderPhone}")
    private String phone;


    public String makeSignature(Long time) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        log.info("makeSignature()");

        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/" + this.serviceId + "/messages";
        String timestamp = time.toString();
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;

    }

    private HttpHeaders makeHeader() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        log.info("makeHeader()");
        Long time = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", makeSignature(time));

        return headers;

    }

    private SmsResponseDto buildRequestDto (SmsDto smsDto, HttpHeaders headers) throws JsonProcessingException, URISyntaxException {
        List<SmsDto> messages = new ArrayList<>();
        messages.add(smsDto);

        SmsRequestDto request = SmsRequestDto.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from(phone)
                .content(smsDto.getContent())
                .messages(messages)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(request);
        HttpEntity<String> httpBody = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        SmsResponseDto response = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/"+ serviceId +"/messages"), httpBody, SmsResponseDto.class);

        return response;
    }


    public Map<String, Object> sendComebackMessage(SmsDto smsDto) throws JsonProcessingException, RestClientException, URISyntaxException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        log.info("sendComebackMessage()");
        HttpHeaders headers = makeHeader();

        SmsResponseDto response = buildRequestDto(smsDto, headers);
        Map<String, Object> map = new HashMap<>();

        if(response.getStatusCode().equals("202")){
//            UserDto userDto = new UserDto();
//            userDto.setNo(smsDTO.getNo());
        }
//        map.put("response", response);
        return map;
    }

}