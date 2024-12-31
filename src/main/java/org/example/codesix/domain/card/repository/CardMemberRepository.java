package org.example.codesix.domain.card.repository;

import org.example.codesix.domain.card.dto.CardMemberUserIdResponseDto;
import org.example.codesix.domain.card.entity.CardMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CardMemberRepository extends JpaRepository<CardMember, Long> {

    @Query("""
        SELECT new org.example.codesix.domain.card.dto.CardMemberUserIdResponseDto(CONCAT('userId:', m.user.id))
        FROM CardMember cm
        JOIN cm.member m
        JOIN cm.card c
        WHERE c.id = :cardId AND c.workList.id = :workListId AND m.workspace.id = :workspaceId
    """)
    List<CardMemberUserIdResponseDto> findCardMemberUserIds(Long workspaceId, Long workListId, Long cardId);
}
