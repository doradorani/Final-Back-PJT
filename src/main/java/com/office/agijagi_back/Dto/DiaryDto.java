package com.office.agijagi_back.Dto;

import com.github.rkpunjal.sqlsafe.SQLInjectionSafe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiaryDto {
    private @SQLInjectionSafe int no;
    private @SQLInjectionSafe String u_email;
    private @SQLInjectionSafe int cd_no;
    private @SQLInjectionSafe int sequence;
    private @SQLInjectionSafe String cd_name;
    private @SQLInjectionSafe String title;
    private @SQLInjectionSafe String content;
    private @SQLInjectionSafe String img;
    private @SQLInjectionSafe String reg_date;
    private @SQLInjectionSafe String mod_date;
}
