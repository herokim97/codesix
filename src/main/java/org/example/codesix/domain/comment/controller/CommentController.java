package org.example.codesix.domain.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.codesix.domain.card.entity.CardMember;
import org.example.codesix.domain.comment.dto.CommentRequestDto;
import org.example.codesix.domain.comment.dto.CommentResponseDto;
import org.example.codesix.domain.comment.service.CommentService;
import org.example.codesix.domain.user.entity.User;
import org.example.codesix.global.auth.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/card/{cardId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long cardId,
                                                            @Valid @RequestBody CommentRequestDto commentRequestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        User user = userDetailsImpl.getUser();
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(cardId,user, commentRequestDto.getContent()));
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> findAllComment(@PathVariable Long cardId) {
        List<CommentResponseDto> comments = commentService.findAllComments(cardId);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CommentResponseDto> udpateComment(@PathVariable Long cardId,
                                                            @PathVariable Long id,
                                                            @Valid @RequestBody CommentRequestDto commentRequestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        User user = userDetailsImpl.getUser();
        return ResponseEntity.ok(commentService.updateComment(cardId,id, user, commentRequestDto.getContent()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long cardId,
                                                @PathVariable Long id,
                                                @AuthenticationPrincipal UserDetailsImpl userDetailsImpl){
        User user = userDetailsImpl.getUser();
        commentService.deleteCard(cardId,id,user);
        return ResponseEntity.status(HttpStatus.OK).body("댓글이 삭제되었습니다.");
    }
}
