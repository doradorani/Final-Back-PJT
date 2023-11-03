package com.office.agijagi_back.Dto;

import com.github.rkpunjal.sqlsafe.SQLInjectionSafe;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {

    private @SQLInjectionSafe int no;
    private @SQLInjectionSafe String name;
    private @SQLInjectionSafe String nickname;
    private @SQLInjectionSafe String address;
    private @SQLInjectionSafe String address_detail1;
    private @SQLInjectionSafe String address_detail2;
    private @SQLInjectionSafe String zip_code;
    private @SQLInjectionSafe String email;
    private @SQLInjectionSafe String phone;
    private @SQLInjectionSafe String status;
    private @SQLInjectionSafe String role;
    private @SQLInjectionSafe String reg_date;
    private @SQLInjectionSafe String mode_date;


    public UserDto(int no, String userName) {
        this.no = no;
        this.email = userName;
    }
}
