package com.office.agijagi_back.Util.SMS;

import com.github.rkpunjal.sqlsafe.SQLInjectionSafe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsRequestDto {
    private @SQLInjectionSafe String type;
    private @SQLInjectionSafe String contentType;
    private @SQLInjectionSafe String countryCode;
    private @SQLInjectionSafe String from;
    private @SQLInjectionSafe String content;
    private @SQLInjectionSafe List<SmsDto> messages;
}