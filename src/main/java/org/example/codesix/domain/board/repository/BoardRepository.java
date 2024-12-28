package org.example.codesix.domain.board.repository;

import org.example.codesix.domain.board.entity.Board;
import org.example.codesix.global.exception.ExceptionType;
import org.example.codesix.global.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    
    
    default Board findByIdOrElseThrow (Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(ExceptionType.BOARD_NOT_FOUND));
    }

    List<Board> findByWorkspaceId(Long workspaceId);

    @Query("select b.workspace.id from Board b where b.id = :boardId")
    Long findWorkspaceIdById(Long boardId);

}
