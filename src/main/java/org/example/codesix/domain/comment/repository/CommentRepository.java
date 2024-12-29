package org.example.codesix.domain.comment.repository;

import org.example.codesix.domain.comment.entity.Comment;
import org.example.codesix.global.exception.ExceptionType;
import org.example.codesix.global.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {


    List<Comment> findAllByCardId(Long cardId);

    default Comment findByIdOrElseThrow (Long id){
        return findById(id)
                .orElseThrow(()-> new NotFoundException(ExceptionType.COMMENT_NOT_FOUND));
    }
}
