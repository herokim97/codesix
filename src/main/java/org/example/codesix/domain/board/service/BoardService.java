package org.example.codesix.domain.board.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.codesix.domain.board.dto.BoardRequestDto;
import org.example.codesix.domain.board.dto.BoardResponseDto;
import org.example.codesix.domain.board.entity.Board;
import org.example.codesix.domain.board.repository.BoardRepository;
import org.example.codesix.domain.notification.entity.Notification;
import org.example.codesix.domain.notification.enums.Type;
import org.example.codesix.domain.notification.repository.NotificationRepository;
import org.example.codesix.domain.notification.service.SlackService;
import org.example.codesix.domain.workspace.entity.Workspace;
import org.example.codesix.domain.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;
    private final SlackService slackService;

    public BoardResponseDto createBoard(Long workspaceId, BoardRequestDto requestDto) {
        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);

        Board board = new Board(workspace,
                                requestDto.getTitle(),
                                requestDto.getBackgroundColor(),
                                requestDto.getBackgroundImage());
        Board savedBoard = boardRepository.save(board);
        slackService.callSlackApi(workspace.getTitle(), savedBoard.getTitle(), Type.BOARD_ADD, workspace);
        return BoardResponseDto.toDto(savedBoard);
    }
    @Transactional
    public List<BoardResponseDto>getBoards(Long workspaceId){
        List<Board> boards = boardRepository.findByWorkspaceId(workspaceId);
        return boards.stream()
                .map(BoardResponseDto::toDto)
                .toList();
    }
    @Transactional
    public BoardResponseDto updateBoard(Long workspaceId, Long boardId, BoardRequestDto requestDto){
        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);
        Board board= boardRepository.findByIdOrElseThrow(boardId);
        slackService.callSlackApi(workspace.getTitle(), board.getTitle(), Type.BOARD_UPDATE, workspace);
        board.updateBoard(requestDto.getTitle(), requestDto.getBackgroundColor(), requestDto.getBackgroundImage());
        Board savedBoard = boardRepository.save(board);
        return BoardResponseDto.toDto(savedBoard);
    }
    @Transactional
    public void deleteBoard(Long workspaceId, Long boardId){
        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);
        Board board = boardRepository.findByIdOrElseThrow(boardId);
        boardRepository.delete(board);
        slackService.callSlackApi(workspace.getTitle(), board.getTitle(), Type.BOARD_DELETE, workspace);
}


}
