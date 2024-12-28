package org.example.codesix.domain.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.codesix.domain.card.entity.CardHistory;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CardHistoryResponseDto {
    private Long id;
    private Long cardId;
    private Long userId;
    private String message;
    private LocalDateTime createdAt;

    public static CardHistoryResponseDto toDto(CardHistory history) {
        return new CardHistoryResponseDto(
                history.getId(),
                history.getCard().getId(),
                history.getUserId(),
                history.getMessage(),
                history.getCreatedAt()
        );
    }
}