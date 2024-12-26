package org.example.codesix.domain.board.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.codesix.domain.board.dto.BoardRequestDto;
import org.example.codesix.domain.board.dto.BoardResponseDto;
import org.example.codesix.domain.board.entity.Board;
import org.example.codesix.domain.board.repository.BoardRepository;
import org.example.codesix.domain.workspace.entity.Workspace;
import org.example.codesix.domain.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;

    public BoardResponseDto createBoard(Long workspaceId, BoardRequestDto requestDto) {
        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);

        Board board = new Board(workspace, requestDto.getTitle(), requestDto.getBackgroundColor(),
                requestDto.getBackgroundImage());
        board = boardRepository.save(board);

        return new BoardResponseDto(board.getId(), board.getTitle(), board.getBackgroundColor(), board.getBackgroundImage());
    }
    public List<BoardResponseDto>getBoards(Long workspaceId){
        List<Board> boards = boardRepository.findByWorkspaceId(workspaceId);
        return boards.stream()
                .map(board -> new BoardResponseDto(board.getId(),
                                                   board.getTitle(),
                                                   board.getBackgroundColor(),
                                                   board.getBackgroundImage()))
                .toList();
    }
    @Transactional
    public BoardResponseDto updateBoard(Long boardId, BoardRequestDto requestDto){
        Board board= boardRepository.findByIdOrElseThrow(boardId);

        board.updateBoard(requestDto.getTitle(), requestDto.getBackgroundColor(), requestDto.getBackgroundImage());
        Board savedBoard = boardRepository.save(board);

        return new BoardResponseDto(savedBoard.getId(), savedBoard.getTitle(), savedBoard.getBackgroundColor(), savedBoard.getBackgroundImage());
    }
    @Transactional
    public void deleteBoard(Long boardId){
        Board board = boardRepository.findByIdOrElseThrow(boardId);
        boardRepository.delete(board);

}


}
