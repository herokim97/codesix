package org.example.codesix.domain.workspace.dto;

import lombok.Getter;
import org.example.codesix.domain.workspace.entity.Member;
import org.example.codesix.domain.workspace.enums.Part;

import java.util.List;

@Getter
public class MemberResponseDto {

    private final Long memeberId;

    private final Long workspaceId;

    private final Long userId;

    private final Part part;

    public MemberResponseDto(Long memeberId,
                             Long workspaceId,
                             Long userId,
                             Part part) {
        this.memeberId = memeberId;
        this.workspaceId = workspaceId;
        this.userId = userId;
        this.part = part;
    }

    public static MemberResponseDto toDto (Member member) {
        return new MemberResponseDto(member.getId(),
                                     member.getWorkspace().getId(),
                                     member.getUser().getId(),
                                     member.getPart()
        );
    }
}
