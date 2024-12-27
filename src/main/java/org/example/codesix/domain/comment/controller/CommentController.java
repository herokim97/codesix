package org.example.codesix.domain.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.codesix.domain.comment.dto.CommentRequestDto;
import org.example.codesix.domain.comment.dto.CommentResponseDto;
import org.example.codesix.domain.comment.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/card{cardId}/commnets")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long cardId,
                                                            @Valid @RequestBody CommentRequestDto commentRequestDto) {
        Long userId = 1L;
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createConmment(userId, commentRequestDto));
    }

}
