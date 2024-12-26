package org.example.codesix.domain.card.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.codesix.domain.card.dto.CardRequestDto;
import org.example.codesix.domain.card.dto.CardResponseDto;
import org.example.codesix.domain.card.service.CardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/list/{cardListId}/cards")
public class CardController {
    private final CardService cardService;

    @PostMapping
    public ResponseEntity<CardResponseDto> createCard(@PathVariable Long cardListId,
                                                      @Valid @RequestBody CardRequestDto cardRequestDto) {
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

    @GetMapping("/{id}")
    public ResponseEntity<CardResponseDto> findCard(@PathVariable Long cardListId,
                                                    @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(cardService.findCard(cardListId, id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CardResponseDto> updateCard(@PathVariable Long cardListId,
                                                      @PathVariable Long id,
                                                      @RequestBody CardRequestDto cardRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(cardService.updateCard(cardListId, id, cardRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCard(@PathVariable Long cardListId,
                                             @PathVariable Long id) {
        cardService.deleteCard(cardListId, id);
        return ResponseEntity.status(HttpStatus.OK).body("카드가 삭제되었습니다.");
    }
}

