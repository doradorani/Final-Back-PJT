package com.office.agijagi_back.Dto;

import com.github.rkpunjal.sqlsafe.SQLInjectionSafe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChildNoteDto {

    private @SQLInjectionSafe int no;
    private @SQLInjectionSafe int cd_no;
    private @SQLInjectionSafe String cd_name;
    private @SQLInjectionSafe String u_email;
    private @SQLInjectionSafe float height;
    private @SQLInjectionSafe float weight;
    private @SQLInjectionSafe float head;
    private @SQLInjectionSafe String vaccination_nm;
    private @SQLInjectionSafe String inoculation_order;
    private @SQLInjectionSafe String inoculation_agency;
    private @SQLInjectionSafe String etc;
    private @SQLInjectionSafe int status;
    private @SQLInjectionSafe String reg_date;
    private @SQLInjectionSafe String mod_date;

}
