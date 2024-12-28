package org.example.codesix.domain.card.service;

import org.example.codesix.domain.card.dto.CardDetailsResponseDto;
import org.example.codesix.domain.card.dto.CardHistoryResponseDto;
import org.example.codesix.domain.card.entity.CardHistory;
import org.example.codesix.domain.card.entity.CardMember;
import org.example.codesix.domain.comment.dto.CommentResponseDto;
import org.example.codesix.domain.user.entity.User;
import org.example.codesix.domain.worklist.entity.WorkList;
import org.example.codesix.domain.worklist.repository.WorkListRepository;
import org.example.codesix.global.exception.ForbiddenException;
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
import java.util.List;


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

    @Transactional(readOnly = true)
    public CardDetailsResponseDto findCard(Long workListId, Long id) {
        cardRepository.findWorkAndList(workListId,id);
        Card card = cardRepository.findCardWithDetails(id, workListId);
        List<Long> userIds = card.getCardMembers().stream()
                .map(member -> member.getMember().getId())
                .toList();

        // 카드와 댓글 데이터를 DTO로 변환
        List<CommentResponseDto> comments = card.getComments().stream()
                .map(CommentResponseDto::toDto)
                .toList();

        // 댓글 데이터를 포함하여 CardResponseDto 반환
        return CardDetailsResponseDto.toDtoWithComments(card,userIds, comments);
    }

    @Transactional
    public CardResponseDto updateCard(Long workListId, User user, Long id, CardRequestDto requestDto) {
        Card card = cardRepository.findWorkAndList(id, workListId);
        validateCardOwner(card, user);
        card.addHistory(createCardHistory(card, "카드 수정", user.getId()));
        card.update(requestDto.getTitle(), requestDto.getDescription(), requestDto.getDueDate());
        return CardResponseDto.toDto(card);
    }

    public void deleteCard(Long workListId, User user, Long id) {
        Card card = cardRepository.findWorkAndList(id, workListId);
        validateCardOwner(card, user);
        cardRepository.delete(card);
    }

    private CardHistory createCardHistory(Card card, String message, Long userId) {
        return new CardHistory(card, message, userId);
    }

    private void validateCardOwner(Card card, User user) {
        boolean isOwner = card.getCardMembers().stream()
                .anyMatch(member -> member.getMember().getUser().getId().equals(user.getId()));
        if (!isOwner) {
            throw new ForbiddenException(ExceptionType.FORBIDDEN_ACTION);
        }
    }

    public List<CardHistoryResponseDto> getHistoryByCardId(Long cardId) {
        List<CardHistory> history = cardRepository.findHistoryByCardId(cardId);
        return history.stream()
                .map(CardHistoryResponseDto::toDto)
                .toList();
    }
}
