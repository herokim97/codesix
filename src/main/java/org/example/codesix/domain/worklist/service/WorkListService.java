package org.example.codesix.domain.worklist.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.codesix.domain.board.entity.Board;
import org.example.codesix.domain.board.repository.BoardRepository;
import org.example.codesix.domain.notification.enums.Type;
import org.example.codesix.domain.notification.service.SlackService;
import org.example.codesix.domain.worklist.dto.WorkListRequestDto;
import org.example.codesix.domain.worklist.dto.WorkListResponseDto;
import org.example.codesix.domain.worklist.entity.WorkList;
import org.example.codesix.domain.worklist.repository.WorkListRepository;
import org.example.codesix.domain.workspace.entity.Workspace;
import org.example.codesix.domain.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkListService {

    private final WorkListRepository workListRepository;
    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;
    private final SlackService slackService;


    @Transactional
    public WorkListResponseDto createWorkList(Long workspaceId, Long boardId,
                                              WorkListRequestDto dto) {
        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);
        Board board = boardRepository.findByIdOrElseThrow(boardId);
        WorkList workList = new WorkList(board, dto.getTitle());
        workListRepository.save(workList);
        slackService.callSlackApi(board.getTitle(), workList.getTitle(), Type.WORK_LIST_ADD, workspace);
        return WorkListResponseDto.toDto(workList);
    }


    @Transactional
    public WorkListResponseDto getWorkList(Long workListId) {
        WorkList workList = workListRepository.findByIdOrElseThrow(workListId);
        return WorkListResponseDto.toDto(workList);

    }
    @Transactional
    public List<WorkListResponseDto> getWorkLists(Long boardId) {
        workListRepository.findByIdOrElseThrow(boardId);
        List<WorkList> worklists = workListRepository.findAllByBoardId(boardId);
        return worklists.stream().map(WorkListResponseDto ::toDto).toList();
    }

    @Transactional
    public WorkListResponseDto updateList(Long workspaceId,
                                          Long boardId,
                                          Long workListId,
                                          WorkListRequestDto dto) {

        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);
        WorkList workList = workListRepository.findByIdOrElseThrow(workListId);
        Board board = boardRepository.findByIdOrElseThrow(boardId);

        workList.updateList(dto.getTitle());
        WorkList savedWorkList = workListRepository.save(workList);

        slackService.callSlackApi(board.getTitle(), savedWorkList.getTitle(), Type.WORK_LIST_UPDATE, workspace);
        return new WorkListResponseDto(savedWorkList.getId(),
                                       savedWorkList.getTitle(),
                                       savedWorkList.getSequence());
    }

    @Transactional
    public void deleteList(Long workspaceId,
                           Long boardId,
                           Long workListId) {

        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);
        Board board = boardRepository.findByIdOrElseThrow(boardId);
        WorkList workList = workListRepository.findByIdOrElseThrow(workListId);

        workListRepository.delete(workList);

        slackService.callSlackApi(board.getTitle(), workList.getTitle(), Type.WORK_LIST_DELETE, workspace);
    }
}
