package org.example.codesix.domain.card.repository;

import org.example.codesix.domain.card.entity.CardFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CardFileRepository extends JpaRepository<CardFile, Long> {

    @Query("SELECT cf.url FROM CardFile cf WHERE cf.card.id = :cardId")
    List<String> findByCardId(Long cardId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CardFile cf WHERE cf.card.id = :cardId")
    void deleteByCardId(Long cardId);
}
