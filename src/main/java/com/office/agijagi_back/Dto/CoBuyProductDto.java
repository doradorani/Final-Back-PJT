package com.office.agijagi_back.Dto;

import com.github.rkpunjal.sqlsafe.SQLInjectionSafe;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CoBuyProductDto {

    private @SQLInjectionSafe int no;
    private @SQLInjectionSafe String id;
    private @SQLInjectionSafe String name;
    private @SQLInjectionSafe String price;
    private @SQLInjectionSafe String start_date;
    private @SQLInjectionSafe String end_date;
    private @SQLInjectionSafe String img;
    private @SQLInjectionSafe String option1;
    private @SQLInjectionSafe String option2;
    private @SQLInjectionSafe String option3;
    private @SQLInjectionSafe String option4;
    private @SQLInjectionSafe String option5;
    private @SQLInjectionSafe String content;
    private @SQLInjectionSafe int min_num;
    private @SQLInjectionSafe int hit;
    private @SQLInjectionSafe int status;
    private @SQLInjectionSafe String reg_date;
    private @SQLInjectionSafe String mod_date;

    private @SQLInjectionSafe int accumulate;

    public CoBuyProductDto() {

    }
}
