package org.example.codesix.domain.comment.service;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.example.codesix.domain.card.entity.Card;
import org.example.codesix.domain.card.entity.CardMember;
import org.example.codesix.domain.card.repository.CardRepository;
import org.example.codesix.domain.comment.dto.CommentRequestDto;
import org.example.codesix.domain.comment.dto.CommentResponseDto;
import org.example.codesix.domain.comment.entity.Comment;
import org.example.codesix.domain.comment.repository.CommentRepository;
import org.example.codesix.domain.user.entity.User;
import org.example.codesix.global.exception.ExceptionType;
import org.example.codesix.global.exception.ForbiddenException;
import org.example.codesix.global.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;

    public CommentResponseDto createComment(Long cardId, User user, String content) {
        CardMember cardMember = cardRepository.findByCardMemberIdOrElseThrow(user.getId());

        Card card = cardRepository.findByIdOrElseThrow(cardId);

        Comment comment = new Comment(card,cardMember,content);
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
        cardRepository.findByIdOrElseThrow(cardId);
        Comment comment = commentRepository.findByIdOrElseThrow(id);
        if(!comment.getCard().getId().equals(user.getId())) {
            throw new ForbiddenException(ExceptionType.FORBIDDEN_ACTION);
        }

        comment.update(content);
        return CommentResponseDto.toDto(comment);
    }

    public void deleteCard(Long cardId, Long id, User user) {
        cardRepository.findByIdOrElseThrow(cardId);
        Comment comment = commentRepository.findByIdOrElseThrow(id);
        if(!comment.getCard().getId().equals(user.getId())) {
            throw new ForbiddenException(ExceptionType.FORBIDDEN_ACTION);
        }
        commentRepository.delete(comment);
    }
}
