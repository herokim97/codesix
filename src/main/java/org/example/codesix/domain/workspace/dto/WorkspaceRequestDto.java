package org.example.codesix.domain.workspace.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class WorkspaceRequestDto {

    @NotNull
    private final String title;

    private final String description;

    public WorkspaceRequestDto(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
