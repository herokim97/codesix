package org.example.codesix.domain.card.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.codesix.domain.card.dto.CardRequestDto;
import org.example.codesix.domain.card.dto.CardResponseDto;
import org.example.codesix.domain.card.entity.Card;
import org.example.codesix.domain.card.repository.CardRepository;
import org.example.codesix.domain.list.entity.CardList;
import org.example.codesix.domain.list.repository.CardListRepository;
import org.example.codesix.global.exception.ExceptionType;
import org.example.codesix.global.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final CardListRepository cardListRepository;

    public CardResponseDto createCard(Long cardListId, CardRequestDto cardRequestDto) {
        CardList cardList = cardListRepository.findById(cardListId).orElseThrow(() -> new NotFoundException(ExceptionType.LIST_NOT_FOUND));
        Card card = new Card(cardList, cardRequestDto.getTitle(), cardRequestDto.getDescription(), cardRequestDto.getDueDate());
        Card cardSave = cardRepository.save(card);
        return CardResponseDto.toDto(cardSave);
    }

    public Page<CardResponseDto> findAllCards(Long cardListId, String title, LocalDate dueDate, String description, Long cardUserId, Pageable pageable) {
        Page<Card> cards = cardRepository.findAllCard(cardListId, title, dueDate, description, cardUserId, pageable);
        return cards.map(CardResponseDto::toDto);
    }

    public CardResponseDto findCard(Long cardListId, Long id) {
        Card card = cardRepository.findCardAndList(id, cardListId);
        return CardResponseDto.toDto(card);
    }

    @Transactional
    public CardResponseDto updateCard(Long cardListId, Long id, CardRequestDto requestDto) {
        Card card = cardRepository.findCardAndList(id, cardListId);
        card.update(requestDto.getTitle(), requestDto.getDescription(), requestDto.getDueDate());
        return CardResponseDto.toDto(card);
    }


    public void deleteCard(Long cardListId, Long id) {
        Card card = cardRepository.findCardAndList(id, cardListId);
        cardRepository.delete(card);
    }
}
