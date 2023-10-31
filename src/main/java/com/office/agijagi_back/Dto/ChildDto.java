package com.office.agijagi_back.Dto;

import com.github.rkpunjal.sqlsafe.SQLInjectionSafe;
import lombok.Data;

@Data
public class ChildDto {
    private @SQLInjectionSafe int no;
    private @SQLInjectionSafe String u_no;
    private @SQLInjectionSafe String name;
    private @SQLInjectionSafe String birth_date;
    private @SQLInjectionSafe String reg_date;
    private @SQLInjectionSafe String mod_date;
}
