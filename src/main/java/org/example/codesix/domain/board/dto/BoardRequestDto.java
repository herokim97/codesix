package org.example.codesix.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BoardRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    private String backgroundColor;


}