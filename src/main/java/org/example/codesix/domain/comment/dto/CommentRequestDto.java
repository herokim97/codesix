package org.example.codesix.domain.comment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentRequestDto {

    @NotNull
    @Size(max = 255 ,message="댓글은 최대 255자 까지 작성 가능합니다.")
    private String content;
}
