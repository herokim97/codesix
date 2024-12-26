package org.example.codesix.domain.card.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class CardRequestDto {

    @NotBlank
    @Size(max = 255)
    private String title;

    private String description;

    @Future(message = "마감일은 지난날 일수 없습니다.")
    private LocalDate dueDate;

}
