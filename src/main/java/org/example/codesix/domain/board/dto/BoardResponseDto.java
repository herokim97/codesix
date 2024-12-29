package org.example.codesix.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.codesix.domain.board.entity.Board;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String backgroundColor;
    private String backgroundImage;

    public static BoardResponseDto toDto(Board board) {
        return new BoardResponseDto(board.getId(),
                                    board.getTitle(),
                                    board.getBackgroundColor(),
                                    board.getBackgroundImage());
    }
}
