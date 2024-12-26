package org.example.codesix.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionType {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND,  "해당 유저의 정보를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,  "해당 멤버의 정보를 찾을 수 없습니다."),
    WORKSPACE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 워크스페이스의 정보를 찾을 수 없습니다."),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND,  "해당 보드의 정보를 찾을 수 없습니다."),
    LIST_NOT_FOUND(HttpStatus.NOT_FOUND,  "해당 리스트의 정보를 찾을 수 없습니다."),
    CARD_NOT_FOUND(HttpStatus.NOT_FOUND,  "해당 카드의 정보를 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,  "해당 댓글의 정보를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    ExceptionType(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
