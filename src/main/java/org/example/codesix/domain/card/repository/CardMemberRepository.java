package org.example.codesix.domain.card.repository;

import org.example.codesix.domain.card.entity.CardMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CardMemberRepository extends JpaRepository<CardMember, Long> {

    @Query("""
        SELECT u.id
        FROM CardMember cm
        JOIN cm.member m
        JOIN m.user u
        JOIN m.workspace w
        WHERE cm.card.id = :cardId
          AND cm.card.workList.id = :workListId
          AND w.id = :workspaceId
       """)
    List<Long> findCardMemberUserIds(Long workspaceId, Long workListId, Long cardId);
}
