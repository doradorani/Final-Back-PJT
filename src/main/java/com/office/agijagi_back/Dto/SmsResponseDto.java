package com.office.agijagi_back.Dto;

import com.github.rkpunjal.sqlsafe.SQLInjectionSafe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsResponseDto {
    private @SQLInjectionSafe String requestId;
    private @SQLInjectionSafe LocalDateTime requestTime;
    private @SQLInjectionSafe String statusCode;
    private @SQLInjectionSafe String statusName;
    private @SQLInjectionSafe Map<String, Object> map;
}