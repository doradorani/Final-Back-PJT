package com.office.agijagi_back.Dto;

import com.github.rkpunjal.sqlsafe.SQLInjectionSafe;
import lombok.Data;

@Data
public class AdminDto {

    private @SQLInjectionSafe int no;
    private @SQLInjectionSafe String id;
    private @SQLInjectionSafe String pw;
    private @SQLInjectionSafe String name;
    private @SQLInjectionSafe String address;
    private @SQLInjectionSafe String address_detail1;
    private @SQLInjectionSafe String address_detail2;
    private @SQLInjectionSafe String zip_code;
    private @SQLInjectionSafe String email;
    private @SQLInjectionSafe String phone;
    private @SQLInjectionSafe int grade;
    private @SQLInjectionSafe String role;
    private @SQLInjectionSafe String reg_date;
    private @SQLInjectionSafe String mod_date;

}
