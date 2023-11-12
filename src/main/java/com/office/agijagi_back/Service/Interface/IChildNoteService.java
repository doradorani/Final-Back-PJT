package com.office.agijagi_back.Service.Interface;

import com.office.agijagi_back.Dto.ChildNoteDto;

import java.util.List;
import java.util.Map;

public interface IChildNoteService {

    int registerChildNote(ChildNoteDto childNoteDto);
    Map<String, Object> searchChildNotes(String childNo, String email);
    int modifyChildNote(ChildNoteDto childNoteDto);
    List<ChildNoteDto> searchChildrenInoculationNotes(String username);
    int deleteChildNote(ChildNoteDto childNoteDto);
    ChildNoteDto searchChildHealthNote(ChildNoteDto childNoteDto);
}
