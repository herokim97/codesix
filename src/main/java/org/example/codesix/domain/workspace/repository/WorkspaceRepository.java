package org.example.codesix.domain.workspace.repository;

import org.example.codesix.domain.workspace.entity.Workspace;
import org.example.codesix.global.exception.ExceptionType;
import org.example.codesix.global.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    default Workspace findByIdOrElseThrow (Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(ExceptionType.WORKSPACE_NOT_FOUND));
    }
}
