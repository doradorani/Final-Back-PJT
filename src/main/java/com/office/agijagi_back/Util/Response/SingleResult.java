package com.office.agijagi_back.Util.Response;

import com.github.rkpunjal.sqlsafe.SQLInjectionSafe;
import lombok.Data;

@Data
public class SingleResult<T> extends CommonResult {
    private @SQLInjectionSafe T data;
}
