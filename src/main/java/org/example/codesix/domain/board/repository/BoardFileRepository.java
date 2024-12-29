package org.example.codesix.domain.board.repository;

import org.example.codesix.domain.board.entity.Board;
import org.example.codesix.domain.board.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {
    @Query(value = "SELECT f.url FROM board_file f WHERE f.board_id = :boardId ORDER BY f.created_at DESC LIMIT 1", nativeQuery = true)
    String findLatestUrlByBoardId(@Param("boardId") Long boardId);

}
