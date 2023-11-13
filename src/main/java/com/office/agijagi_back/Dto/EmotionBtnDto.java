package com.office.agijagi_back.Dto;

import com.github.rkpunjal.sqlsafe.SQLInjectionSafe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmotionBtnDto {

    private @SQLInjectionSafe int no;
    private @SQLInjectionSafe String user_mail;
    private @SQLInjectionSafe int post_no;

}
