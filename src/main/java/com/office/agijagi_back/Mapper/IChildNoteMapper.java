package com.office.agijagi_back.Mapper;


import com.office.agijagi_back.Dto.ChildNoteDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IChildNoteMapper {
    List<ChildNoteDto> selectChildrenInoculationNotesByEmail(String email);

    List<ChildNoteDto> selectChildNotesByNoAndEmail(String childNo, String email);

    int insertChildNote(ChildNoteDto childNoteDto);

    Integer deleteChildNote(ChildNoteDto childNoteDto);

    Integer updateChildNote(ChildNoteDto childNoteDto);

    ChildNoteDto selectChildrenHealthNote(ChildNoteDto childNoteDto);
}
