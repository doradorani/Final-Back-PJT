package com.office.agijagi_back.Dto;

import com.github.rkpunjal.sqlsafe.SQLInjectionSafe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostReportDto {

    private @SQLInjectionSafe int no;
    private @SQLInjectionSafe int post_no;
    private @SQLInjectionSafe String post_text;
    private @SQLInjectionSafe String user_mail;
    private @SQLInjectionSafe int post_status;
    private @SQLInjectionSafe String reason;
    private @SQLInjectionSafe String report_user;
    private @SQLInjectionSafe int status;
    private @SQLInjectionSafe String reg_date;
    private @SQLInjectionSafe String mod_date;

}
