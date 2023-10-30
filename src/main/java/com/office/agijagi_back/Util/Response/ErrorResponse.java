package com.office.agijagi_back.Util.Response;

import com.github.rkpunjal.sqlsafe.SQLInjectionSafe;
import lombok.Data;

@Data
public class ErrorResponse {
    private @SQLInjectionSafe int status;
    private @SQLInjectionSafe String message;
    private @SQLInjectionSafe String code;

    public ErrorResponse(ErrorCode errorCode){
        this.status = errorCode.getStatus();
        this.message = errorCode.getMessage();
        this.code = errorCode.getErrorCode();
    }
}
