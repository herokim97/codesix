package org.example.codesix.domain.workspace.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.example.codesix.domain.workspace.enums.Part;

@Getter
public class MemberPartRequestDto {

    @NotNull
    private final Part part;

    public MemberPartRequestDto(Part part) {
        this.part = part;
    }
}
