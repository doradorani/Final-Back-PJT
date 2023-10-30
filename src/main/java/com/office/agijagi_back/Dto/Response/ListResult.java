package com.office.agijagi_back.Dto.Response;

import com.github.rkpunjal.sqlsafe.SQLInjectionSafe;
import lombok.Data;

import java.util.List;

@Data
public class ListResult<T> extends CommonResult {
    private @SQLInjectionSafe List<T> data;
}
