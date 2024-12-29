package org.example.codesix.domain.card.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.codesix.domain.card.dto.*;
import org.example.codesix.domain.card.service.CardService;
import org.example.codesix.domain.user.entity.User;
import org.example.codesix.global.auth.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workSpace/{workSpaceId}/workList/{workListId}/cards")
public class CardController {
    private final CardService cardService;

    @PostMapping
    public ResponseEntity<CardResponseDto> createCard(@PathVariable Long workSpaceId,
                                                      @PathVariable Long workListId,
                                                      @Valid @RequestBody CardRequestDto cardRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.createCard(workListId, cardRequestDto));
    }

    @GetMapping
    public ResponseEntity<Page<CardResponseDto>> findAllCards(@PathVariable Long workSpaceId,
                                                              @PathVariable Long workListId,
                                                              @RequestParam(value = "title", required = false) String title,
                                                              @RequestParam(value = "dueDate", required = false) LocalDate dueDate,
                                                              @RequestParam(value = "description", required = false) String description,
                                                              @RequestParam(value = "cardUserId", required = false) Long cardUserId,
                                                              Pageable pageable) {
        Page<CardResponseDto> cards = cardService.findAllCards(workListId, title, dueDate, description, cardUserId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(cards);
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<CardDetailsResponseDto> findCard(@PathVariable Long workSpaceId,
                                                           @PathVariable Long workListId,
                                                           @PathVariable Long cardId) {
        CardDetailsResponseDto cardDetailsResponseDto = cardService.findCard(workListId, cardId);
        return ResponseEntity.status(HttpStatus.OK).body(cardDetailsResponseDto);
    }

    @PatchMapping("/{cardId}")
    public ResponseEntity<CardResponseDto> updateCard(@PathVariable Long workSpaceId,
                                                      @PathVariable Long workListId,
                                                      @PathVariable Long cardId,
                                                      @Valid @RequestBody CardRequestDto cardRequestDto,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        User user = userDetailsImpl.getUser();
        return ResponseEntity.status(HttpStatus.OK).body(cardService.updateCard(workListId, user, cardId, cardRequestDto));
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<String> deleteCard(@PathVariable Long workSpaceId,
                                             @PathVariable Long workListId,
                                             @PathVariable Long cardId,
                                             @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        User user = userDetailsImpl.getUser();
        cardService.deleteCard(workListId, user, cardId);
        return ResponseEntity.status(HttpStatus.OK).body("카드가 삭제되었습니다.");
    }

    @GetMapping("/{cardId}/details")
    public ResponseEntity<List<CardHistoryResponseDto>> findCardDetails(@PathVariable Long workSpaceId,
                                                                        @PathVariable Long workListId,
                                                                        @PathVariable Long cardId){
        List<CardHistoryResponseDto> history = cardService.getHistoryByCardId(workListId,cardId);
        return ResponseEntity.status(HttpStatus.OK).body(history);
    }

    @GetMapping("/{cardId}/files")
    public ResponseEntity<List<CardFileResponseDto>> findCardFiles(@PathVariable Long workSpaceId,
                                                                   @PathVariable Long workListId,
                                                  @PathVariable Long cardId) {
        List<CardFileResponseDto> cardFileResponseDto = cardService.findCardFiles(workListId,cardId);
        return ResponseEntity.status(HttpStatus.OK).body(cardFileResponseDto);
    }

    @PostMapping("/{cardId}/files")
    public ResponseEntity<String> uploadFile(@PathVariable Long workSpaceId,
                                             @PathVariable Long workListId,
                                             @PathVariable Long cardId,
                                             @RequestParam("file") MultipartFile file,
                                             @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        User user = userDetailsImpl.getUser();
        String fileUrl = cardService.uploadFile(workListId,cardId, file, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(fileUrl);
    }

    @DeleteMapping("/{cardId}/files/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable Long workSpaceId,
                                             @PathVariable Long workListId,
                                             @PathVariable Long cardId,
                                             @PathVariable Long fileId,
                                             @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        User user = userDetailsImpl.getUser();
        cardService.deleteFile(workListId,cardId, fileId, user);
        return ResponseEntity.status(HttpStatus.OK).body("파일이 삭제되었습니다.");
    }

    @PostMapping("/{cardId}/cardMembers")
    public ResponseEntity<String> CreateMember(@PathVariable Long workSpaceId,
                                               @PathVariable Long workListId,
                                               @PathVariable Long cardId,
                                               @RequestBody CardMemberRequestDto cardMemberRequestDto){
        cardService.createCardMember(workSpaceId,workListId,cardId,cardMemberRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("카드멤버가 추가되었습니다.");
    }

    @GetMapping("/{cardId}/cardMembers")
    public ResponseEntity<List<Long>> findAllCardMembers(@PathVariable Long workSpaceId,
                                                              @PathVariable Long workListId,
                                                              @PathVariable Long cardId){
        List <Long> userIds = cardService.findAllCardMembers(workSpaceId,workListId,cardId);
        return ResponseEntity.status(HttpStatus.OK).body(userIds);
    }

    @DeleteMapping("{cardId}/cardMembers/{cardMemberId}")
    public ResponseEntity<String> deleteCardMember(@PathVariable Long workSpaceId,
                                                   @PathVariable Long workListId,
                                                   @PathVariable Long cardId,
                                                   @PathVariable Long cardMemberId){
        cardService.deleteCardMember(workListId,cardId,cardMemberId);
        return ResponseEntity.status(HttpStatus.OK).body("카드멤버가 삭제되었습니다.");
    }
}

