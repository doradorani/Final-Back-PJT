package com.office.agijagi_back.Dto;

import com.github.rkpunjal.sqlsafe.SQLInjectionSafe;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@AllArgsConstructor
public class DiaryDto {
    private @SQLInjectionSafe int no;
    private @SQLInjectionSafe String u_email;
    private @SQLInjectionSafe int cd_no;
    private @SQLInjectionSafe String title;
    private @SQLInjectionSafe String content;
    private @SQLInjectionSafe String img;
    private @SQLInjectionSafe String reg_date;
    private @SQLInjectionSafe String mod_date;
}
