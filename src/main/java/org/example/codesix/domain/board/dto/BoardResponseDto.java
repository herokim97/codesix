package org.example.codesix.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String backgroundColor;
    private String backgroundImage;

}
