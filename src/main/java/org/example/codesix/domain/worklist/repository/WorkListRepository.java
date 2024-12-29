package org.example.codesix.domain.worklist.repository;

import org.example.codesix.domain.board.entity.Board;
import org.example.codesix.domain.worklist.entity.WorkList;
import org.example.codesix.global.exception.ExceptionType;
import org.example.codesix.global.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkListRepository extends JpaRepository<WorkList, Long> {
    default WorkList findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(ExceptionType.WORKLIST_NOT_FOUND));

    }
    List<WorkList> findAllByBoardId(Long boardId);

    @Query("select w.board.workspace.id from WorkList w where w.id = :workListId")
    Long findWorkspaceIdById(Long workListId);
}
