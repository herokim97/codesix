package org.example.codesix.domain.card.service;

import lombok.extern.slf4j.Slf4j;
import org.example.codesix.domain.card.dto.*;
import org.example.codesix.domain.card.entity.CardFile;
import org.example.codesix.domain.card.entity.CardHistory;
import org.example.codesix.domain.card.entity.CardMember;
import org.example.codesix.domain.card.repository.CardFileRepository;
import org.example.codesix.domain.card.repository.CardMemberRepository;
import org.example.codesix.domain.comment.dto.CommentResponseDto;
import org.example.codesix.domain.notification.enums.Type;
import org.example.codesix.domain.notification.service.SlackService;
import org.example.codesix.domain.user.entity.User;
import org.example.codesix.domain.worklist.entity.WorkList;
import org.example.codesix.domain.worklist.repository.WorkListRepository;
import org.example.codesix.domain.workspace.entity.Member;
import org.example.codesix.domain.workspace.entity.Workspace;
import org.example.codesix.domain.workspace.repository.MemberRepository;
import org.example.codesix.domain.workspace.repository.WorkspaceRepository;
import org.example.codesix.global.exception.ForbiddenException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.codesix.domain.card.entity.Card;
import org.example.codesix.domain.card.repository.CardRepository;
import org.example.codesix.global.exception.ExceptionType;
import org.example.codesix.global.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final WorkListRepository workListRepository;
    private final CardFileRepository cardFileRepository;
    private final CardFileUploadService cardFileUploadService;
    private final CardMemberRepository cardMemberRepository;
    private final MemberRepository memberRepository;
    private final WorkspaceRepository workspaceRepository;
    private final SlackService slackService;

    public CardResponseDto createCard(Long workspaceId, Long workListId, CardRequestDto cardRequestDto) {

        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);
        WorkList workList = workListRepository.findByIdOrElseThrow(workListId);

        Card card = new Card(workList, cardRequestDto.getTitle(), cardRequestDto.getDescription(), cardRequestDto.getDueDate());
        Card cardSave = cardRepository.save(card);

        slackService.callSlackApi(workList.getTitle(), cardSave.getTitle(), Type.CARD_ADD, workspace);

        return CardResponseDto.toDto(cardSave);
    }

    public Page<CardResponseDto> findAllCards(Long workListId,
                                              String title,
                                              LocalDate dueDate,
                                              String description,
                                              Long cardUserId,
                                              Pageable pageable) {

        Page<Card> cards = cardRepository.findAllCard(workListId, title, dueDate, description, cardUserId, pageable);

        return cards.map(CardResponseDto::toDto);
    }

    @Transactional(readOnly = true)
    public CardDetailsResponseDto findCard(Long workListId, Long id) {

        cardRepository.findWorkAndList(workListId, id);
        List<String> cardFileUrls = cardFileRepository.findByCardId(id);
        Card card = cardRepository.findCardWithDetails(id, workListId);

        if (card == null) {
            throw new NotFoundException(ExceptionType.CARD_DETAILS_NOT_FOUND);
        }

        List<Long> userIds = card.getCardMembers().stream()
                .map(member -> member.getMember().getId())
                .toList();

        List<CommentResponseDto> comments = card.getComments().stream()
                .map(CommentResponseDto::toDto)
                .toList();

        return CardDetailsResponseDto.toDtoWithComments(card, userIds, comments, cardFileUrls);
    }

    @Transactional
    public CardResponseDto updateCard(Long workspaceId, Long workListId, User user, Long id, CardRequestDto requestDto) {

        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);
        Card card = cardRepository.findWorkAndList(workListId, id);
        WorkList workList = workListRepository.findByIdOrElseThrow(workListId);

        validateCardOwner(card, user);

        slackService.callSlackApi(workList.getTitle(), card.getTitle(), Type.CARD_UPDATE, workspace);

        card.addHistory(createCardHistory(card, "카드 수정", user.getId()));
        card.update(requestDto.getTitle(), requestDto.getDescription(), requestDto.getDueDate());

        return CardResponseDto.toDto(card);
    }

    @Transactional
    public void deleteCard(Long workspaceId, Long workListId, User user, Long id) {

        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);
        WorkList workList = workListRepository.findByIdOrElseThrow(workListId);

        Card card = cardRepository.findWorkAndList(workListId, id);

        cardFileRepository.deleteByCardId(id);

        validateCardOwner(card, user);

        cardRepository.delete(card);
        slackService.callSlackApi(workList.getTitle(), card.getTitle(), Type.CARD_DELETE, workspace);
    }

    public String uploadFile(Long workListId, Long cardId, MultipartFile file, User user) {
        Card card = cardRepository.findWorkAndList(workListId, cardId);
        validateCardOwner(card, user);
        CardFile cardFile;
        try {
            cardFile = cardFileUploadService.uploadFileAndSaveMetadata(card, file);
        } catch (IOException e) {
            log.error("파일 업로드 실패: {}", e.getMessage(), e);
            throw new RuntimeException("파일 업로드 실패: " + e.getMessage(), e);
        }
        cardFileRepository.save(cardFile);
        return cardFile.getUrl();
    }

    public void deleteFile(Long workListId, Long cardId, Long fileId, User user) {
        cardRepository.findWorkAndList(workListId, cardId);
        CardFile cardFile = cardFileRepository.findById(fileId)
                .orElseThrow(() -> new NotFoundException(ExceptionType.FILE_NOT_FOUND));
        validateCardOwner(cardFile.getCard(), user);

        // S3에서 파일 삭제
        cardFileUploadService.deleteFile(cardFile.getUrl());

        // DB에서 파일 메타데이터 삭제
        cardFileRepository.delete(cardFile);
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

    public List<CardHistoryResponseDto> getHistoryByCardId(Long workListId, Long cardId) {
        cardRepository.findWorkAndList(workListId, cardId);
        List<CardHistory> history = cardRepository.findHistoryByCardId(cardId);
        return history.stream()
                .map(CardHistoryResponseDto::toDto)
                .toList();
    }

    public List<CardFileResponseDto> findCardFiles(Long workListId, Long cardId) {
        cardRepository.findWorkAndList(workListId, cardId);
        List<CardFile> cardFiles = cardRepository.findByWorkListAndCard(workListId, cardId);
        if (cardFiles.isEmpty()) {
            throw new NotFoundException(ExceptionType.FILE_NOT_FOUND);
        }
        return cardFiles.stream().map(CardFileResponseDto::toDto).toList();
    }

    public void createCardMember(Long workspaceId, Long workListId, Long cardId, CardMemberRequestDto requestDto) {
        Card card = cardRepository.findWorkAndList(workListId, cardId);
        Member member = memberRepository.findByIdOrElseThrow(requestDto.getMemberId());
        if (!member.getWorkspace().getId().equals(workspaceId)) {
            throw new ForbiddenException(ExceptionType.MEMBER_NOT_IN_WORKSPACE);
        }
        CardMember cardMember = new CardMember();
        cardMember.setCard(card);
        cardMember.setMember(member);
        cardMemberRepository.save(cardMember);
    }

    public List<Long> findAllCardMembers(Long workspaceId, Long workListId, Long cardId) {
        cardRepository.findWorkAndList(workListId, cardId);
        return cardMemberRepository.findCardMemberUserIds(workspaceId, workListId, cardId);
    }

    public void deleteCardMember(Long workListId, Long cardId, Long cardMemberId) {
        cardRepository.findWorkAndList(workListId, cardId);
        cardMemberRepository.deleteById(cardMemberId);
    }
}
