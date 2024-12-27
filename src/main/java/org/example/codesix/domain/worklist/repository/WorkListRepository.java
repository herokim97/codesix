package org.example.codesix.domain.worklist.repository;

import org.example.codesix.domain.board.entity.Board;
import org.example.codesix.domain.worklist.entity.WorkList;
import org.example.codesix.global.exception.ExceptionType;
import org.example.codesix.global.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkListRepository extends JpaRepository<WorkList, Long> {
    default WorkList findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(ExceptionType.WORKLIST_NOT_FOUND));
    }
}
