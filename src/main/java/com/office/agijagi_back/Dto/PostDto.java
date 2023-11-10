package com.office.agijagi_back.Dto;

import com.github.rkpunjal.sqlsafe.SQLInjectionSafe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private @SQLInjectionSafe int no;
    private @SQLInjectionSafe String post_text;
    private @SQLInjectionSafe String imgs_path;
    private @SQLInjectionSafe String user_mail;
    private @SQLInjectionSafe String nickname;
    private @SQLInjectionSafe String img;
    private @SQLInjectionSafe int like_cnt;
    private @SQLInjectionSafe int great_cnt;
    private @SQLInjectionSafe int sad_cnt;
    private @SQLInjectionSafe int reply_cnt;
    private @SQLInjectionSafe int status;
    private @SQLInjectionSafe String reg_date;
    private @SQLInjectionSafe String mod_date;

}
