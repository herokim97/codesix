package org.example.codesix.domain.card.repository;

import org.example.codesix.domain.card.entity.Card;
import org.example.codesix.domain.card.entity.CardMember;
import org.example.codesix.global.exception.ExceptionType;
import org.example.codesix.global.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    default Card findByIdOrElseThrow(Long cardId) {
        return findById(cardId)
                .orElseThrow(() -> new NotFoundException(ExceptionType.CARD_NOT_FOUND));
    }

    @Query("SELECT cm FROM CardMember cm WHERE cm.id = :cardMemberId")
    Optional<CardMember> findByCardMemberId(Long cardMemberId);

    default CardMember findByCardMemberIdOrElseThrow(Long cardMemberId) {
        return findByCardMemberId(cardMemberId)
                .orElseThrow(() -> new NotFoundException(ExceptionType.CARD_MEMBER_NOT_FOUND));
    }

    @Query("""
                SELECT c FROM Card c
                LEFT JOIN c.cardMembers cm
                LEFT JOIN cm.member m
                WHERE c.cardList.id = :cardListId
                AND (:title IS NULL OR LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%')))
                AND (:dueDate IS NULL OR c.dueDate = :dueDate)
                AND (:description IS NULL OR LOWER(c.description) LIKE LOWER(CONCAT('%', :description, '%')))
                AND (:cardUserId IS NULL OR m.id = :cardUserId)
            """)
    Page<Card> findAllCard(Long cardListId, String title, LocalDate dueDate, String description, Long cardUserId, Pageable pageable);


    Optional<Card> findByIdAndCardListId(Long id, Long cardListId);

    default Card findCardAndList(Long id, Long cardListId) {
        return this.findByIdAndCardListId(id, cardListId)
                .orElseThrow(() -> new NotFoundException(ExceptionType.LIST_OR_CARD_NOT_FOUND));
    }

}
