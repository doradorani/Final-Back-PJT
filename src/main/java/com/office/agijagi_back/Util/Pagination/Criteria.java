package com.office.agijagi_back.Util.Pagination;

import lombok.Data;

@Data
public class Criteria {

    private int pageNum;        // 현제 페이지
    private int amount;         // 한페이지당 출력되는 아이템(게시물) 개수
    private int skip;           // skip할 아이템 개수

    public Criteria() {
        this(1, 20);
        this.skip = 0;
    }

    public Criteria(int pageNum, int amount) {
        this.pageNum = pageNum;
        this.amount = amount;
        this.skip = (pageNum - 1) * amount;
    }

}