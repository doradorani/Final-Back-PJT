package com.office.agijagi_back.Mapper;

import com.office.agijagi_back.Dto.ChildDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IDiaryMapper {
    int insertChildInfo(ChildDto childDto);
}
