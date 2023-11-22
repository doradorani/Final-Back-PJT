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
    private @SQLInjectionSafe String admin_id;
    private @SQLInjectionSafe int attach_cnt;
    private @SQLInjectionSafe String file_name;
    private @SQLInjectionSafe String attach_path;
    private @SQLInjectionSafe int hit;
    private @SQLInjectionSafe int status;
    private @SQLInjectionSafe String reg_date;
    private @SQLInjectionSafe String mod_date;

}
