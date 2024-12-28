package org.example.codesix.domain.board.controller;

import lombok.RequiredArgsConstructor;
import org.example.codesix.domain.board.dto.BoardRequestDto;
import org.example.codesix.domain.board.dto.BoardResponseDto;
import org.example.codesix.domain.board.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workspaces/{workspaceId}/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    //보드 생성
    @PostMapping
    public ResponseEntity<BoardResponseDto> createBoard(@PathVariable Long workspaceId,
                                                        @RequestBody BoardRequestDto requestdto) {
        BoardResponseDto boardResponseDto = boardService.createBoard(workspaceId, requestdto);
        return ResponseEntity.status(HttpStatus.CREATED).body(boardResponseDto);
    }

    //보드  조회
    @GetMapping
    public ResponseEntity<List<BoardResponseDto>> getBoards(@PathVariable Long workspaceId) {
        List<BoardResponseDto> boards = boardService.getBoards(workspaceId);
        return ResponseEntity.ok(boards);
    }

    //보드 수정
    @PatchMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable Long boardId,
                                                        @RequestBody BoardRequestDto requestdto) {
        BoardResponseDto boardResponseDto = boardService.updateBoard(boardId, requestdto);
        return ResponseEntity.ok(boardResponseDto);
    }

    //보드 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.noContent().build();

    }
}


