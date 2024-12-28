package org.example.codesix.domain.card.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.codesix.domain.card.dto.CardDetailsResponseDto;
import org.example.codesix.domain.card.dto.CardHistoryResponseDto;
import org.example.codesix.domain.card.dto.CardRequestDto;
import org.example.codesix.domain.card.dto.CardResponseDto;
import org.example.codesix.domain.card.service.CardService;
import org.example.codesix.domain.user.entity.User;
import org.example.codesix.global.auth.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workList/{cardListId}/cards")
public class CardController {
    private final CardService cardService;

    @PostMapping
    public ResponseEntity<CardResponseDto> createCard(@PathVariable Long cardListId,
                                                      @Valid @RequestBody CardRequestDto cardRequestDto,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.createCard(cardListId, cardRequestDto));
    }

    @GetMapping
    public ResponseEntity<Page<CardResponseDto>> findAllCards(@PathVariable Long cardListId,
                                                              @RequestParam(value = "title", required = false) String title,
                                                              @RequestParam(value = "dueDate", required = false) LocalDate dueDate,
                                                              @RequestParam(value = "description", required = false) String description,
                                                              @RequestParam(value = "cardUserId", required = false) Long cardUserId,
                                                              Pageable pageable) {
        Page<CardResponseDto> cards = cardService.findAllCards(cardListId, title, dueDate, description, cardUserId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(cards);
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<CardDetailsResponseDto> findCard(@PathVariable Long cardListId,
                                                           @PathVariable Long cardId) {
        CardDetailsResponseDto cardDetailsResponseDto = cardService.findCard(cardListId, cardId);
        return ResponseEntity.status(HttpStatus.OK).body(cardDetailsResponseDto);
    }

    @PatchMapping("/{cardId}")
    public ResponseEntity<CardResponseDto> updateCard(@PathVariable Long cardListId,
                                                      @PathVariable Long cardId,
                                                      @Valid @RequestBody CardRequestDto cardRequestDto,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        User user = userDetailsImpl.getUser();
        return ResponseEntity.status(HttpStatus.OK).body(cardService.updateCard(cardListId, user, cardId, cardRequestDto));
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<String> deleteCard(@PathVariable Long cardListId,
                                             @PathVariable Long cardId,
                                             @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        User user = userDetailsImpl.getUser();
        cardService.deleteCard(cardListId, user, cardId);
        return ResponseEntity.status(HttpStatus.OK).body("카드가 삭제되었습니다.");
    }

    @GetMapping("/{cardId}/details")
    public ResponseEntity<List<CardHistoryResponseDto>> findCardDetails(@PathVariable Long cardId){
        List<CardHistoryResponseDto> history = cardService.getHistoryByCardId(cardId);
        return ResponseEntity.status(HttpStatus.OK).body(history);
    }
}

