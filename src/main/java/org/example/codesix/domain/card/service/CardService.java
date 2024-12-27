package org.example.codesix.domain.card.service;

import org.example.codesix.domain.worklist.entity.WorkList;
import org.example.codesix.domain.worklist.repository.WorkListRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.codesix.domain.card.dto.CardRequestDto;
import org.example.codesix.domain.card.dto.CardResponseDto;
import org.example.codesix.domain.card.entity.Card;
import org.example.codesix.domain.card.repository.CardRepository;
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
    private final WorkListRepository workListRepository;

    public CardResponseDto createCard(Long workListId, CardRequestDto cardRequestDto) {
        WorkList workList = workListRepository.findById(workListId).orElseThrow(() -> new NotFoundException(ExceptionType.WORKLIST_NOT_FOUND));
        Card card = new Card(workList, cardRequestDto.getTitle(), cardRequestDto.getDescription(), cardRequestDto.getDueDate());
        Card cardSave = cardRepository.save(card);
        return CardResponseDto.toDto(cardSave);
    }

    public Page<CardResponseDto> findAllCards(Long workListId, String title, LocalDate dueDate, String description, Long cardUserId, Pageable pageable) {
        Page<Card> cards = cardRepository.findAllCard(workListId, title, dueDate, description, cardUserId, pageable);
        return cards.map(CardResponseDto::toDto);
    }

    public CardResponseDto findCard(Long workListId, Long id) {
        Card card = cardRepository.findWorkAndList(id, workListId);
        return CardResponseDto.toDto(card);
    }

    @Transactional
    public CardResponseDto updateCard(Long workListId, Long id, CardRequestDto requestDto) {
        Card card = cardRepository.findWorkAndList(id, workListId);
        card.update(requestDto.getTitle(), requestDto.getDescription(), requestDto.getDueDate());
        return CardResponseDto.toDto(card);
    }


    public void deleteCard(Long workListId, Long id) {
        Card card = cardRepository.findWorkAndList(id, workListId);
        cardRepository.delete(card);
    }
}
