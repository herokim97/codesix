package org.example.codesix.domain.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.codesix.domain.card.entity.Card;
import org.example.codesix.domain.comment.dto.CommentResponseDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class CardResponseDto {
    private Long id;
    private Long cardListId;
    private String title;
    private String description;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static CardResponseDto toDto(Card card) {
        return new CardResponseDto(
                card.getId(),
                card.getWorkList().getId(),
                card.getTitle(),
                card.getDescription(),
                card.getDueDate(),
                card.getCreatedAt(),
                card.getModifiedAt()
        );
    }

}
