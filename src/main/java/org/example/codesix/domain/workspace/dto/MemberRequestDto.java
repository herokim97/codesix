package org.example.codesix.domain.workspace.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class MemberRequestDto {

    @NotNull
    private final List<String> emails;

    public MemberRequestDto(List<String> emails) {
        this.emails = emails;
    }
}
