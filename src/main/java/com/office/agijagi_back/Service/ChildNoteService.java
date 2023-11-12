package com.office.agijagi_back.Service;

import com.office.agijagi_back.Dto.ChildDto;
import com.office.agijagi_back.Dto.ChildNoteDto;
import com.office.agijagi_back.Mapper.IChildNoteMapper;
import com.office.agijagi_back.Mapper.IDiaryMapper;
import com.office.agijagi_back.Service.Interface.IChildNoteService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class ChildNoteService implements IChildNoteService {
    private final IChildNoteMapper childNoteMapper;
    private final IDiaryMapper diaryMapper;

    public ChildNoteService(IChildNoteMapper childNoteMapper, IDiaryMapper diaryMapper) {
        this.childNoteMapper = childNoteMapper;
        this.diaryMapper = diaryMapper;
    }

    @Override
    public int registerChildNote(ChildNoteDto childNoteDto) {
        log.info("[ChildNoteService] registerChildNote");

        return childNoteMapper.insertChildNote(childNoteDto);
    }

    @Override
    public Map<String, Object> searchChildNotes(String childNo, String email) {
        log.info("[ChildNoteService] searchChildNotes");

        Map<String, Object> result = new HashMap<>();

        List<ChildDto> childDtos = diaryMapper.selectChildrenByEmail(email);
        List<ChildNoteDto> childNoteDtos = childNoteMapper.selectChildNotesByNoAndEmail(childNo, email);
        result.put("childDtos", childDtos);
        result.put("childNoteDtos", childNoteDtos);
        return result;
    }


    @Override
    public List<ChildNoteDto> searchChildrenInoculationNotes(String email) {
        log.info("[ChildNoteService] searchChildrenInoculationNotes");
        return childNoteMapper.selectChildrenInoculationNotesByEmail(email);
    }

    @Override
    public int deleteChildNote(ChildNoteDto childNoteDto){
        log.info("[ChildNoteService] deleteChildNote");
        return childNoteMapper.deleteChildNote(childNoteDto);
    }

    @Override
    public ChildNoteDto searchChildHealthNote(ChildNoteDto childNoteDto) {
        return null;
    }

    @Override
    public int modifyChildNote(ChildNoteDto childNoteDto){
        log.info("[ChildNoteService] modifyChildNote");
        return childNoteMapper.updateChildNote(childNoteDto);
    }
}
