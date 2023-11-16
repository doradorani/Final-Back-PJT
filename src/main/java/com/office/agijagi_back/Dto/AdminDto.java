package com.office.agijagi_back.Dto;

import com.github.rkpunjal.sqlsafe.SQLInjectionSafe;
import lombok.Data;

@Data
public class AdminDto {

    private @SQLInjectionSafe int no;
    private @SQLInjectionSafe String id;
    private @SQLInjectionSafe String pw;
    private @SQLInjectionSafe String name;
    private @SQLInjectionSafe String email;
    private @SQLInjectionSafe String phone;
    private @SQLInjectionSafe int grade;
    private @SQLInjectionSafe String reg_date;
    private @SQLInjectionSafe String mod_date;

    private @SQLInjectionSafe String currentId;

    public AdminDto(String id, String pw) {
        this.id = id;
        this.pw = pw;
    }

    public AdminDto(String id, String pw, String name, String email, String phone) {
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }


    public AdminDto(int no, String id, String pw, String name, String email, String phone, int grade, String reg_date, String mod_date) {
        this.no = no;
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.grade = grade;
        this.reg_date = reg_date;
        this.mod_date = mod_date;
    }


    public AdminDto(Object id, Object pw, Object name, Object email, Object phone, Object currentId) {
        this.id = (String) id;
        this.pw = (String) pw;
        this.name = (String) name;
        this.email = (String) email;
        this.phone = (String) phone;
        this.currentId = (String) currentId;
    }
}
