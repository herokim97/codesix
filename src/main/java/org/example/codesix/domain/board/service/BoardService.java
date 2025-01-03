package org.example.codesix.domain.board.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.codesix.domain.board.dto.BoardFileResponseDto;
import org.example.codesix.domain.board.dto.BoardRequestDto;
import org.example.codesix.domain.board.dto.BoardResponseDto;
import org.example.codesix.domain.board.entity.Board;
import org.example.codesix.domain.board.entity.BoardFile;
import org.example.codesix.domain.board.repository.BoardFileRepository;
import org.example.codesix.domain.board.repository.BoardRepository;
import org.example.codesix.domain.card.entity.Card;
import org.example.codesix.domain.card.entity.CardFile;
import org.example.codesix.domain.user.entity.User;
import org.example.codesix.domain.notification.entity.Notification;
import org.example.codesix.domain.notification.enums.Type;
import org.example.codesix.domain.notification.repository.NotificationRepository;
import org.example.codesix.domain.notification.service.SlackService;
import org.example.codesix.domain.workspace.entity.Workspace;
import org.example.codesix.domain.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;
    private final BoardFileRepository boardFileRepository;
    private final BoardFileUploadService BoardFileUploadService;
    private final SlackService slackService;

    public BoardResponseDto createBoard(Long workspaceId, BoardRequestDto requestDto) {
        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);
        Board board = new Board(workspace,
                requestDto.getTitle(),
                requestDto.getBackgroundColor());
        Board savedBoard = boardRepository.save(board);
        slackService.callSlackApi(workspace.getTitle(), savedBoard.getTitle(), Type.BOARD_ADD, workspace);
        return new BoardResponseDto(board.getId(), board.getTitle(), board.getBackgroundColor());
    }


    @Transactional
    public List<BoardResponseDto> getBoards(Long workspaceId) {
        List<Board> boards = boardRepository.findByWorkspaceId(workspaceId);
        return boards.stream()
                .map(board -> new BoardResponseDto(
                        board.getId(),
                        board.getTitle(),
                        board.getBackgroundColor()))
                .collect(Collectors.toList());
    }

    @Transactional
    public BoardResponseDto updateBoard(Long workspaceId, Long boardId, BoardRequestDto requestDto) {
        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);
        Board board = boardRepository.findByIdOrElseThrow(boardId);
        slackService.callSlackApi(workspace.getTitle(), board.getTitle(), Type.BOARD_UPDATE, workspace);
        board.updateBoard(requestDto.getTitle(), requestDto.getBackgroundColor());
        Board savedBoard = boardRepository.save(board);

        return new BoardResponseDto(savedBoard.getId(), savedBoard.getTitle(), savedBoard.getBackgroundColor());
    }

    @Transactional
    public void deleteBoard(Long workspaceId, Long boardId) {
        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);
        Board board = boardRepository.findByIdOrElseThrow(boardId);
        boardRepository.delete(board);
        slackService.callSlackApi(workspace.getTitle(), board.getTitle(), Type.BOARD_DELETE, workspace);
    }



    public String uploadFile(Long boardId, MultipartFile file) {
        BoardFile boardFile;
        Board board = boardRepository.findByIdOrElseThrow(boardId);
        try {
            boardFile = BoardFileUploadService.uploadFileAndSaveMetadata(board, file);
        } catch (IOException e) {
            log.error("파일 업로드 실패: {}", e.getMessage(), e);
            throw new RuntimeException("파일 업로드 실패: " + e.getMessage(), e);
        }
        boardFileRepository.save(boardFile);
        return boardFile.getUrl();
    }


    public BoardFileResponseDto findBoard(Long boardId) {
        Board board = boardRepository.findByIdOrElseThrow(boardId);
        String fileUrl = boardFileRepository.findLatestUrlByBoardId(boardId);
        return  BoardFileResponseDto.toDtoBoardFile(board,fileUrl);
    }
    }
