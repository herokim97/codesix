package org.example.codesix.domain.worklist.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.codesix.domain.user.repository.UserRepository;
import org.example.codesix.domain.worklist.dto.WorkListRequestDto;
import org.example.codesix.domain.worklist.dto.WorkListResponseDto;
import org.example.codesix.domain.worklist.service.WorkListService;
import org.example.codesix.domain.workspace.entity.Member;
import org.example.codesix.domain.workspace.enums.Part;
import org.example.codesix.domain.workspace.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workspaces/{workspaceId}/boards/{boardId}/workLists")
@RequiredArgsConstructor
public class WorkListController {

    private final WorkListService worklistservice;

    @PostMapping
    public ResponseEntity<WorkListResponseDto> createWorkList(@PathVariable Long workspaceId,
                                                              @PathVariable Long boardId,
                                                              @RequestBody WorkListRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(worklistservice.createWorkList(workspaceId, boardId,dto));
    }
    @GetMapping("/{workListId}")
    public ResponseEntity<WorkListResponseDto> getWorkList(@PathVariable Long workListId) {
        WorkListResponseDto worklistresponseDto = worklistservice.getWorkList(workListId);
        return ResponseEntity.ok(worklistresponseDto);
    }
//    @GetMapping
//    public ResponseEntity<List<WorkListResponseDto>>getWorkLists(@PathVariable Long boardId){
//        List<WorkListResponseDto> workListsResponseDto = worklistservice.getWorkLists(boardId);
//        return ResponseEntity.ok(workListsResponseDto);
//    }

    @PatchMapping("/{workListId}")
    public ResponseEntity<WorkListResponseDto> updateWorkList(@PathVariable Long workspaceId,
                                                              @PathVariable Long boardId,
                                                              @PathVariable Long workListId,
                                                              @RequestBody WorkListRequestDto dto) {
        WorkListResponseDto worklistResponseDto = worklistservice.updateList(workspaceId, boardId, workListId, dto);
        return ResponseEntity.ok(worklistResponseDto);
    }
    @DeleteMapping("/{workListId}")
    public ResponseEntity<Void> deleteWorkList(@PathVariable Long workspaceId,
                                               @PathVariable Long boardId,
                                               @PathVariable Long workListId) {
        worklistservice.deleteList(workspaceId,boardId,workListId);
        return ResponseEntity.noContent().build();
    }
}
