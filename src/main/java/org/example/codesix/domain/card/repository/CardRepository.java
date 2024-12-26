package org.example.codesix.domain.card.repository;

import org.example.codesix.domain.card.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface CardRepository extends JpaRepository<Card, Long> {

    @Query("""
        SELECT c FROM Card c
        LEFT JOIN c.cardMembers cm
        LEFT JOIN cm.member m
        WHERE c.cardList.id = :cardListId
        AND (:title IS NULL OR LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%')))
        AND (:dueDate IS NULL OR c.dueDate = :dueDate)
        AND (:description IS NULL OR LOWER(c.description) LIKE LOWER(CONCAT('%', :description, '%')))
        AND (:user IS NULL OR m.id = :user)
    """)
    Page<Card> findAllCard(Long cardListId, String title, LocalDate dueDate, String description, Long user, Pageable pageable);
}
