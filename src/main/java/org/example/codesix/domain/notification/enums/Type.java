package org.example.codesix.domain.notification.enums;

import lombok.Getter;

@Getter
public enum Type {
    BOARD_ADD("보드가 추가되었습니다."),
    BOARD_UPDATE("보드가 수정되었습니다."),
    BOARD_DELETE("보드가 삭제되었습니다."),
    WORK_LIST_ADD("워크리스트가 추가되었습니다."),
    WORK_LIST_UPDATE("워크리스트가 수정되었습니다."),
    WORK_LIST_DELETE("워크리스트가 삭제되었습니다."),
    CARD_ADD("카드가 추가되었습니다."),
    CARD_UPDATE("카드가 수정되었습니다."),
    CARD_DELETE("카드가 삭제되었습니다."),
    MEMBER_ADD("멤버가 추가되었습니다."),
    MEMBER_DELETE("멤버가 삭제되었습니다."),
    CARD_MEMBER_ADD("카드멤버가 추가되었습니다."),
    CARD_MEMBER_DELETE("카드멤버가 삭제되었습니다."),
    COMMENT_ADD("의 코멘트가 달렸습니다."),
    COMMENT_UPDATE("의 코멘트가 수정되었습니다."),
    COMMENT_DELETE("의 코멘트가 삭제되었습니다.");

    private final String message;

    Type(String message) {
        this.message = message;
    }
}
