package org.example.codesix.domain.worklist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WorkListRequestDto {

    private Long id;
    private String title;
    private String content;
}
