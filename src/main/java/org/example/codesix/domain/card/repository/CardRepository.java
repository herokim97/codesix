package org.example.codesix.domain.card.repository;

import org.example.codesix.domain.card.entity.Card;
import org.example.codesix.domain.card.entity.CardFile;
import org.example.codesix.domain.card.entity.CardHistory;
import org.example.codesix.domain.card.entity.CardMember;
import org.example.codesix.global.exception.ExceptionType;
import org.example.codesix.global.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    default Card findByIdOrElseThrow(Long cardId) {
        return findById(cardId)
                .orElseThrow(() -> new NotFoundException(ExceptionType.CARD_NOT_FOUND));
    }

    @Query("""
        SELECT cm
        FROM CardMember cm
        JOIN cm.member m
        JOIN m.user u
        WHERE cm.card.id = :cardId AND u.id = :userId
    """)
    Optional<CardMember> findByCardMemberId(Long cardId, Long userId);

    default CardMember findByCardMemberIdOrElseThrow(Long cardId, Long userId) {
        return findByCardMemberId(cardId,userId)
                .orElseThrow(() -> new NotFoundException(ExceptionType.CARD_MEMBER_NOT_FOUND));
    }

    @Query("""
                SELECT c FROM Card c
                LEFT JOIN c.cardMembers cm
                LEFT JOIN cm.member m
                WHERE c.workList.id = :workListId
                AND (:title IS NULL OR LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%')))
                AND (:dueDate IS NULL OR c.dueDate = :dueDate)
                AND (:description IS NULL OR LOWER(c.description) LIKE LOWER(CONCAT('%', :description, '%')))
                AND (:cardUserId IS NULL OR m.id = :cardUserId)
            """)
    Page<Card> findAllCard(Long workListId, String title, LocalDate dueDate, String description, Long cardUserId, Pageable pageable);


    @Query("""
            SELECT c
            FROM Card c
            WHERE c.id = :id AND c.workList.id = :workListId
            """)
    Optional<Card> findByIdAndWorkListId(Long workListId, Long id);

    default Card findWorkAndList(Long workListId, Long cardId) {
        return this.findByIdAndWorkListId(workListId, cardId)
                .orElseThrow(() -> new NotFoundException(ExceptionType.LIST_OR_CARD_NOT_FOUND));
    }

    @Query("select c.workList.board.workspace.id from Card c where c.id = :cardId")
    Long findWorkspaceIdById(Long cardId);

    @Query("SELECT DISTINCT c FROM Card c " +
            "LEFT JOIN FETCH c.comments co " +
            "LEFT JOIN FETCH c.cardMembers cm " +
            "WHERE c.id = :cardId AND c.workList.id = :workListId")
    Card findCardWithDetails(@Param("cardId") Long cardId, @Param("workListId") Long workListId);

    @Query("SELECT h FROM CardHistory h WHERE h.card.id = :cardId")
    List<CardHistory> findHistoryByCardId(Long cardId);

    @Query("""
            SELECT cf
            FROM CardFile cf
            LEFT JOIN cf.card c
            LEFT JOIN c.workList wl
            WHERE wl.id = :workListId
              AND c.id = :cardId
            """)
    List<CardFile> findByWorkListAndCard(@Param("workListId") Long workListId, @Param("cardId") Long cardId);
}
