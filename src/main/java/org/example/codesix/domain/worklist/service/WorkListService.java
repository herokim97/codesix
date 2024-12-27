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

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkListService {

    private final WorkListRepository workListRepository;
    private final BoardRepository boardRepository;


    @Transactional
    public WorkListResponseDto createWorkList(@PathVariable Long boardId,
                                              @RequestBody WorkListRequestDto dto) {
        Board board = boardRepository.findByIdOrElseThrow(boardId);
        WorkList workList = new WorkList(board,dto.getTitle(),dto.getContent());
        workListRepository.save(workList);
        return new WorkListResponseDto(workList.getId(),workList.getTitle(),workList.getContent(),
                workList.getSequence());
    }


    @Transactional
    public WorkListResponseDto getWorkList(@PathVariable Long boardId) {
        workListRepository.findByIdOrElseThrow(boardId);
        return new WorkListResponseDto();

    }
    @Transactional
    public WorkListResponseDto updateList(@PathVariable Long boardId,
                                              @RequestBody WorkListRequestDto dto) {
        WorkList worklist = workListRepository.findByIdOrElseThrow(boardId);
        worklist.updateList(dto.getId(),dto.getTitle());
        WorkList savedWorkList = workListRepository.save(worklist);
        return new WorkListResponseDto(savedWorkList.getId(),savedWorkList.getTitle(), savedWorkList.getContent(),
                savedWorkList.getSequence());
    }
    @Transactional
    public void deleteList(@PathVariable Long listId) {
        WorkList worklist = workListRepository.findByIdOrElseThrow(listId);
        workListRepository.delete(worklist);


    }
}
