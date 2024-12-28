package org.example.codesix.domain.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.codesix.domain.card.entity.Card;
import org.example.codesix.domain.comment.dto.CommentResponseDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class CardDetailsResponseDto {
    private Long id;
    private Long cardListId;
    private String title;
    private String description;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<Long> userIds;

    private List<CommentResponseDto> comments;
    public static CardDetailsResponseDto toDtoWithComments(Card card, List<Long> userIds,List<CommentResponseDto> comments) {
        return new CardDetailsResponseDto(
                card.getId(),
                card.getWorkList().getId(),
                card.getTitle(),
                card.getDescription(),
                card.getDueDate(),
                card.getCreatedAt(),
                card.getModifiedAt(),
                new ArrayList<>(new HashSet<>(userIds)),
                comments

        );
    }

}
