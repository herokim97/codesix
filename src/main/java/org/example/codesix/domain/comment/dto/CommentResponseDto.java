package org.example.codesix.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class CommentResponseDto {

    private Long id;
    private Long cardId;
    private Long cardMemberId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
