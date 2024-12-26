package org.example.codesix.domain.card.service;

import lombok.RequiredArgsConstructor;
import org.example.codesix.domain.card.dto.CardRequestDto;
import org.example.codesix.domain.card.dto.CardResponseDto;
import org.example.codesix.domain.card.entity.Card;
import org.example.codesix.domain.card.repository.CardRepository;
import org.example.codesix.domain.list.entity.CardList;
import org.example.codesix.domain.list.repository.CardListRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final CardListRepository cardListRepository;

    public CardResponseDto createCard(Long cardListId, CardRequestDto cardRequestDto) {
        CardList cardList = cardListRepository.findById(cardListId).orElseThrow(()-> new NoSuchElementException("해당리스트가 존재하지않습니다."));
        Card card = new Card(cardList,cardRequestDto.getTitle(),cardRequestDto.getDescription(),cardRequestDto.getDueDate());
        Card cardSave = cardRepository.save(card);
        return  CardResponseDto.toDto(cardSave);
    }

    public Page<CardResponseDto> findAllCards(Long cardListId, String title, LocalDate dueDate, String description, Long user, Pageable pageable) {
        Page<Card> cards = cardRepository.findAllCard(cardListId,title,dueDate,description,user,pageable);
        return cards.map(CardResponseDto::toDto);
    }

}
