package org.example.codesix.domain.workspace.controller;

import lombok.RequiredArgsConstructor;
import org.example.codesix.domain.workspace.dto.*;
import org.example.codesix.domain.workspace.service.WorkspaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    //워크스페이스 생성 API
    // @param userId     -------> 로그인 중인 워크스페이스 ADMIN의 정보를 가져와야함
    @PostMapping("/workspaces")
    public ResponseEntity<WorkspaceResponseDto> createWorkspace(@RequestBody WorkspaceRequestDto requestDto) {
        //임의의 userId 설정 (이후 쿠키에서 유저 id를 가져올것)
        Long userId = 1L;
        return ResponseEntity.status(HttpStatus.CREATED).body(workspaceService.createWorkspace(userId, requestDto));
    }

    //워크스페이스 단건 조회 API
    @GetMapping("/workspaces/{id}")
    public ResponseEntity<WorkspaceResponseDto> findWorkspace(@PathVariable Long id) {
        return ResponseEntity.ok().body(workspaceService.findWorkspace(id));
    }

    //워크스페이스 전체 조회 API
    @GetMapping("/workspaces")
    public ResponseEntity<List<WorkspaceResponseDto>> findAll() {
        return ResponseEntity.ok().body(workspaceService.findAll());
    }

    //워크스페이스 수정 API
    @PatchMapping("/workspaces/{id}")
    public ResponseEntity<WorkspaceResponseDto> updateWorkspace(@PathVariable Long id,
                                                                @RequestBody WorkspaceRequestDto requestDto) {
        return ResponseEntity.ok().body(workspaceService.updateWorkspace(id, requestDto));
    }

    //워크스페이스 삭제 API
    @DeleteMapping("/workspaces/{id}")
    public ResponseEntity<String> deleteWorkspace(@PathVariable Long id) {
        workspaceService.deleteWorkspace(id);
        return ResponseEntity.ok().body("워크스페이스가 삭제되었습니다.");
    }

    //멤버 추가 API
    @PostMapping("/workspaces/{workspaceId}/members")
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
}
