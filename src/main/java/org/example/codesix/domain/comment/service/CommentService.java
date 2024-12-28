package org.example.codesix.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.example.codesix.domain.card.entity.Card;
import org.example.codesix.domain.card.entity.CardHistory;
import org.example.codesix.domain.card.entity.CardMember;
import org.example.codesix.domain.card.repository.CardRepository;
import org.example.codesix.domain.comment.dto.CommentResponseDto;
import org.example.codesix.domain.comment.entity.Comment;
import org.example.codesix.domain.comment.repository.CommentRepository;
import org.example.codesix.domain.user.entity.User;
import org.example.codesix.global.exception.ExceptionType;
import org.example.codesix.global.exception.ForbiddenException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;

    @Transactional
    public CommentResponseDto createComment(Long cardId, User user, String content) {
        CardMember cardMember = cardRepository.findByCardMemberIdOrElseThrow(user.getId());
        Card card = cardRepository.findByIdOrElseThrow(cardId);
        Comment comment = new Comment(card, cardMember, content);
        card.addHistory(createCardHistory(card, "댓글 작성", user.getId()));
        Comment savedComment = commentRepository.save(comment);
        return CommentResponseDto.toDto(savedComment);
    }

    public List<CommentResponseDto> findAllComments(Long cardId) {
        cardRepository.findByIdOrElseThrow(cardId);
        List<Comment> comments = commentRepository.findAllByCardId(cardId);
        return comments.stream().map(CommentResponseDto::toDto).toList();
    }

    @Transactional
    public CommentResponseDto updateComment(Long cardId,Long id, User user, String content) {
        Card card = cardRepository.findByIdOrElseThrow(cardId);
        Comment comment = commentRepository.findByIdOrElseThrow(id);
        validateCommentOwner(comment, user);
        card.addHistory(createCardHistory(card, "댓글 수정", user.getId()));
        comment.update(content);
        return CommentResponseDto.toDto(comment);
    }

    @Transactional
    public void deleteComment(Long cardId, Long id, User user) {
        Card card = cardRepository.findByIdOrElseThrow(cardId);
        Comment comment = commentRepository.findByIdOrElseThrow(id);
        validateCommentOwner(comment, user);
        card.addHistory(createCardHistory(card, "댓글 삭제", user.getId()));
        commentRepository.delete(comment);
    }

    private CardHistory createCardHistory(Card card, String message, Long userId) {
        return new CardHistory(card, message, userId);
    }

    private void validateCommentOwner(Comment comment, User user) {
        // 댓글 작성자와 현재 로그인한 사용자 ID가 일치하는지 확인
        if (!comment.getCardMember().getMember().getId().equals(user.getId())) {
            throw new ForbiddenException(ExceptionType.FORBIDDEN_ACTION);
        }
    }
}
