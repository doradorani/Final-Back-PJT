package com.office.agijagi_back.Dto;

import com.github.rkpunjal.sqlsafe.SQLInjectionSafe;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CoBuyListDto {

    private @SQLInjectionSafe int no;
    private @SQLInjectionSafe int user_no;
    private @SQLInjectionSafe int product_no;
    private @SQLInjectionSafe int status;
    private @SQLInjectionSafe String reg_date;
    private @SQLInjectionSafe String mod_date;

}
