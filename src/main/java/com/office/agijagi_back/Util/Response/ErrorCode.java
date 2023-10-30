package com.office.agijagi_back.Util.Response;

import com.github.rkpunjal.sqlsafe.SQLInjectionSafe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    NOT_FOUND(404,"COMMON-ERR-404","PAGE NOT FOUND"),
    INTERNET_SERVER_ERROR(500,"COMMON-ERR-500","INTERNET SERVER ERROR"),
    NICKNAME_DUPLICATION(400,"MEMBER-ERR-400","NICKNAME DUPLICATED"),
    ;

    private @SQLInjectionSafe int status;
    private @SQLInjectionSafe String errorCode;
    private @SQLInjectionSafe String message;

}
