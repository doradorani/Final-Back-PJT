package com.office.agijagi_back.Dto.Response;

import io.swagger.annotations.ApiModelProperty;
import com.github.rkpunjal.sqlsafe.SQLInjectionSafe;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CommonResult {

    @ApiModelProperty(value = "응답 성공 여부: T/F")
    private @SQLInjectionSafe boolean success;

    @ApiModelProperty(value = "응답 코드: >= 0 정상, < 0 비정상")
    private @SQLInjectionSafe int code;

    @ApiModelProperty(value = "응답 메시지")
    private @SQLInjectionSafe String msg;
}