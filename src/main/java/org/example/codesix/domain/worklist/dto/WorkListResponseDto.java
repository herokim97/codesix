package org.example.codesix.domain.worklist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.codesix.domain.worklist.entity.WorkList;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WorkListResponseDto {

    private Long id;
    private String title;
    private Integer sequence;

    public static WorkListResponseDto toDto (WorkList workList) {
        return new WorkListResponseDto(workList.getId(), workList.getTitle(), workList.getSequence());
    }
}
