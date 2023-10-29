package com.office.agijagi_back.Dto.Response;

import lombok.Data;

@Data
public class SingleResult<T> extends CommonResult {
    private T data;
}
