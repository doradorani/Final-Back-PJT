package com.office.agijagi_back.Dto;

import com.github.rkpunjal.sqlsafe.SQLInjectionSafe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDto {

    private @SQLInjectionSafe int no;
    private @SQLInjectionSafe String title;
    private @SQLInjectionSafe String content;
    private @SQLInjectionSafe int admin_no;
    private @SQLInjectionSafe String admin_name;
    private @SQLInjectionSafe int attach_cnt;
    private @SQLInjectionSafe int hit;
    private @SQLInjectionSafe int status;
    private @SQLInjectionSafe String reg_date;
    private @SQLInjectionSafe String mod_date;

}
