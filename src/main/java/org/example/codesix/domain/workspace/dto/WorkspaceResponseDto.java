package org.example.codesix.domain.workspace.dto;

import lombok.Getter;
import org.example.codesix.domain.workspace.entity.Workspace;

import java.time.LocalDateTime;

@Getter
public class WorkspaceResponseDto {

    private final Long id;

    private final String title;

    private final String description;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    public WorkspaceResponseDto(Long id, String title, String description, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static WorkspaceResponseDto toDto(Workspace workspace) {
        return new WorkspaceResponseDto(
                workspace.getId(),
                workspace.getTitle(),
                workspace.getDescription(),
                workspace.getCreatedAt(),
                workspace.getModifiedAt()
                );
    }
}
