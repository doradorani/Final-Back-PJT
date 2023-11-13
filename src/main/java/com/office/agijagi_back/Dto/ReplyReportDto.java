package com.office.agijagi_back.Dto;

import com.github.rkpunjal.sqlsafe.SQLInjectionSafe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyReportDto {

    private @SQLInjectionSafe int no;
    private @SQLInjectionSafe int reply_no;
    private @SQLInjectionSafe int post_no;
    private @SQLInjectionSafe String comment;
    private @SQLInjectionSafe String user_mail;
    private @SQLInjectionSafe String reason;
    private @SQLInjectionSafe String report_user;
    private @SQLInjectionSafe String post_status;
    private @SQLInjectionSafe String reg_date;
    private @SQLInjectionSafe String mod_date;

}
