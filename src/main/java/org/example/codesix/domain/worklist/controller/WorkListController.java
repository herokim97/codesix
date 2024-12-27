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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards/{boardId}/lists")
@RequiredArgsConstructor
public class WorkListController {

    private final WorkListService worklistservice;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<WorkListResponseDto> createWorkList(@PathVariable Long boardId,
                                                              @RequestBody WorkListRequestDto dto) {
        WorkListResponseDto worklistResponseDto = worklistservice.createWorkList(boardId,dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(worklistResponseDto);

    }
    @GetMapping
    public ResponseEntity<WorkListResponseDto> getWorkList(@PathVariable Long boardId) {
        WorkListResponseDto worklistresponseDto = worklistservice.getWorkList(boardId);
        return ResponseEntity.ok(worklistresponseDto);
    }
    @PatchMapping
    public ResponseEntity<WorkListResponseDto> updateList(@PathVariable Long boardId,
                                                          @RequestBody WorkListRequestDto
                                                          dto){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        Long userId = userRepository.findByEmailOrElseThrow(userName).getId();
        Member member = memberRepository.findByIdOrElseThrow(userId);
        Part memberPart = member.getPart();
        if(Part.READ.equals(memberPart)){
            throw new Error("사용자를 찾을 수 없습니다.");
        }
        WorkListResponseDto worklistResponseDto = worklistservice.updateList(boardId, dto);
        return ResponseEntity.ok(worklistResponseDto);
    }
    @DeleteMapping
    public ResponseEntity<Void> deleteList(@PathVariable Long listId) {
        worklistservice.deleteList(listId);
        return ResponseEntity.noContent().build();
    }

}
