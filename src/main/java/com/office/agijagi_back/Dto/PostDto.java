package com.office.agijagi_back.Dto;

import com.github.rkpunjal.sqlsafe.SQLInjectionSafe;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostDto {

    private @SQLInjectionSafe int no;
    private @SQLInjectionSafe String user_email;
    private @SQLInjectionSafe String text_content;
    private @SQLInjectionSafe String img_content;
//    private @SQLInjectionSafe String hashtag;
    private @SQLInjectionSafe int like_cnt;
    private @SQLInjectionSafe int great_cnt;
    private @SQLInjectionSafe int sad_cnt;
    private @SQLInjectionSafe int reply_cnt;
    private @SQLInjectionSafe String reg_date;
    private @SQLInjectionSafe String mod_date;

}
