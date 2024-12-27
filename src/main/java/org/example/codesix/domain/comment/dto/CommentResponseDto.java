package org.example.codesix.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.codesix.domain.comment.entity.Comment;

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

    public static CommentResponseDto toDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getCard().getId(),
                comment.getCardMember().getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getModifiedAt()
        );
    }
}
