package com.office.agijagi_back.Dto;

import com.github.rkpunjal.sqlsafe.SQLInjectionSafe;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ChildDto {
    private @SQLInjectionSafe int no;
    private @SQLInjectionSafe String name;
    private @SQLInjectionSafe String u_email;
    private @SQLInjectionSafe String img;
    private @SQLInjectionSafe String content;
    private @SQLInjectionSafe String birth_date;
    private @SQLInjectionSafe String reg_date;
    private @SQLInjectionSafe String mod_date;
}
