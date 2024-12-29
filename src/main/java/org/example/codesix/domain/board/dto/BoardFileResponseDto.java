package org.example.codesix.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.codesix.domain.board.entity.Board;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BoardFileResponseDto {
    private Long id;
    private String title;
    private String backgroundColor;
    private String boardFileUrl;

    public static BoardFileResponseDto toDtoBoardFile(Board board, String boardFileUrl) {
        return new BoardFileResponseDto(board.getId(), board.getTitle(),
                board.getBackgroundColor(),boardFileUrl);



    }

}
