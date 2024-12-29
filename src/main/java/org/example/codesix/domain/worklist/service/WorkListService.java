package org.example.codesix.domain.worklist.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.codesix.domain.board.entity.Board;
import org.example.codesix.domain.board.repository.BoardRepository;
import org.example.codesix.domain.worklist.dto.WorkListRequestDto;
import org.example.codesix.domain.worklist.dto.WorkListResponseDto;
import org.example.codesix.domain.worklist.entity.WorkList;
import org.example.codesix.domain.worklist.repository.WorkListRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkListService {

    private final WorkListRepository workListRepository;
    private final BoardRepository boardRepository;


    @Transactional
    public WorkListResponseDto createWorkList(Long boardId,
                                              WorkListRequestDto dto) {
        Board board = boardRepository.findByIdOrElseThrow(boardId);
        WorkList workList = new WorkList(board, dto.getTitle());
        workListRepository.save(workList);
        return WorkListResponseDto.toDto(workList);
    }


    @Transactional
    public WorkListResponseDto getWorkList(Long workListId) {
        WorkList workList = workListRepository.findByIdOrElseThrow(workListId);
        return WorkListResponseDto.toDto(workList);

    }

    @Transactional
    public WorkListResponseDto updateList(Long workListId,
                                          WorkListRequestDto dto) {
        WorkList worklist = workListRepository.findByIdOrElseThrow(workListId);
        worklist.updateList(dto.getTitle());
        WorkList savedWorkList = workListRepository.save(worklist);
        return new WorkListResponseDto(savedWorkList.getId(),
                                       savedWorkList.getTitle(),
                                       savedWorkList.getSequence());
    }

    @Transactional
    public void deleteList(Long workListId) {
        WorkList worklist = workListRepository.findByIdOrElseThrow(workListId);
        workListRepository.delete(worklist);

    }
}
