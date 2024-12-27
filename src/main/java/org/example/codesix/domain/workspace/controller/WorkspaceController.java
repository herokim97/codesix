package org.example.codesix.domain.workspace.controller;

import lombok.RequiredArgsConstructor;
import org.example.codesix.domain.workspace.dto.*;
import org.example.codesix.domain.workspace.service.WorkspaceService;
import org.example.codesix.global.auth.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    //워크스페이스 생성 API
    @PostMapping()
    public ResponseEntity<WorkspaceResponseDto> createWorkspace(@RequestBody WorkspaceRequestDto requestDto,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return ResponseEntity.status(HttpStatus.CREATED).body(workspaceService.createWorkspace(userDetailsImpl.getUser().getId(), requestDto));
    }

    //워크스페이스 단건 조회 API
    @GetMapping("/{id}")
    public ResponseEntity<WorkspaceResponseDto> findWorkspace(@PathVariable Long id) {
        return ResponseEntity.ok().body(workspaceService.findWorkspace(id));
    }

    //워크스페이스 전체 조회 API
    @GetMapping
    public ResponseEntity<List<WorkspaceResponseDto>> findAll() {
        return ResponseEntity.ok().body(workspaceService.findAll());
    }

    //워크스페이스 수정 API
    @PatchMapping("/{id}")
    public ResponseEntity<WorkspaceResponseDto> updateWorkspace(@PathVariable Long id,
                                                                @RequestBody WorkspaceRequestDto requestDto) {
        return ResponseEntity.ok().body(workspaceService.updateWorkspace(id, requestDto));
    }

    //워크스페이스 삭제 API
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWorkspace(@PathVariable Long id) {
        workspaceService.deleteWorkspace(id);
        return ResponseEntity.ok().body("워크스페이스가 삭제되었습니다.");
    }

    //멤버 추가 API
    @PostMapping("/{workspaceId}/members")
    public ResponseEntity<List<MemberResponseDto>> addMember(@PathVariable Long workspaceId,
                                                             @RequestBody MemberRequestDto memberRequestDto) {
        return ResponseEntity.ok().body(workspaceService.addMember(workspaceId, memberRequestDto));
    }

    //멤버 역할 수정 API
    @PatchMapping("/members/{memberId}")
    public ResponseEntity<MemberResponseDto> updateMemberPart(@PathVariable Long memberId,
                                                              @RequestBody MemberPartRequestDto memberPartRequestDto) {

        return ResponseEntity.ok().body(workspaceService.updateMemberPart(memberId,memberPartRequestDto));
    }

    //멤버 전체 조회
    @GetMapping("/{workspaceId}/members")
    public ResponseEntity<List<MemberResponseDto>> findAllMembers(@PathVariable Long workspaceId) {
        return ResponseEntity.ok().body(workspaceService.findAllMembers(workspaceId));
    }

    //멤버 삭제
    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<String> deleteMember(@PathVariable Long memberId){
        workspaceService.deleteMember(memberId);
        return ResponseEntity.ok().body("멤버가 삭제되었습니다.");
    }
}
