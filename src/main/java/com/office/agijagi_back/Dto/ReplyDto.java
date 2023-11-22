package com.office.agijagi_back.Dto;

import com.github.rkpunjal.sqlsafe.SQLInjectionSafe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDto {

    private @SQLInjectionSafe int no;
    private @SQLInjectionSafe String user_mail;
    private @SQLInjectionSafe int post_no;
    private @SQLInjectionSafe int indentation;
    private @SQLInjectionSafe int reply_no;
    private @SQLInjectionSafe String comment;
    private @SQLInjectionSafe int status;
    private @SQLInjectionSafe String reg_date;
    private @SQLInjectionSafe String mod_date;
    private @SQLInjectionSafe String nickname;
    private @SQLInjectionSafe String img;

}
