package org.example.codesix.domain.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.codesix.domain.card.entity.CardFile;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CardFileResponseDto {
    private Long id;
    private Long cardId;
    private String url;
    private Long size;
    private String extension;
    private String filename;
    private LocalDateTime createdAt;

    public static CardFileResponseDto toDto(CardFile cardFile) {
        return new CardFileResponseDto(
                cardFile.getId(),
                cardFile.getCard().getId(),
                cardFile.getUrl(),
                cardFile.getSize(),
                cardFile.getExtension(),
                cardFile.getFilename(),
                cardFile.getCreatedAt()
        );
    }
}
