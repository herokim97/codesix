package org.example.codesix.domain.workspace.service;

import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.codesix.domain.user.entity.User;
import org.example.codesix.domain.user.repository.UserRepository;
import org.example.codesix.domain.workspace.dto.*;
import org.example.codesix.domain.workspace.entity.Member;
import org.example.codesix.domain.workspace.entity.Workspace;
import org.example.codesix.domain.workspace.enums.Part;
import org.example.codesix.domain.workspace.repository.MemberRepository;
import org.example.codesix.domain.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    public WorkspaceResponseDto createWorkspace(Long userId, WorkspaceRequestDto requestDto) {
        User creator = userRepository.findByIdOrElseThrow(userId);
        Workspace workspace = new Workspace(requestDto.getTitle(), requestDto.getDescription(), creator);
        Workspace savedWorkspace = workspaceRepository.save(workspace);
        return WorkspaceResponseDto.toDto(savedWorkspace);
    }

    public WorkspaceResponseDto findWorkspace(Long id) {
        return WorkspaceResponseDto.toDto(findById(id));
    }

    public List<WorkspaceResponseDto> findAll() {
        List<Workspace> workspaces = workspaceRepository.findAll();

        return workspaces.stream().map(WorkspaceResponseDto::toDto).toList();
    }

    @Transactional
    public WorkspaceResponseDto updateWorkspace(Long id, WorkspaceRequestDto requestDto) {
        Workspace workspace = findById(id);
        workspace.update(requestDto.getTitle(), requestDto.getDescription());
        return WorkspaceResponseDto.toDto(workspace);
    }

    @Transactional
    public List<MemberResponseDto> addMember(Long id, MemberRequestDto memberRequestDto) {
        Workspace workspace = findById(id);
        List<User> users = findByEmails(memberRequestDto.getEmails());
        List<Member> members = createMembers(workspace,users);
        return saveMemberAndConvertToDto(members);
    }

    @Transactional
    public void deleteWorkspace(Long id) {
        Workspace workspace = findById(id);
        workspaceRepository.delete(workspace);
    }

    @Transactional
    public MemberResponseDto updateMemberPart(Long memberId, MemberPartRequestDto memberPartRequestDto) {
        Member member = memberRepository.findByIdOrElseThrow(memberId);
        member.updatePart(memberPartRequestDto.getPart());
        return MemberResponseDto.toDto(member);
    }

    public Workspace findById(Long id) {
        return workspaceRepository.findByIdOrElseThrow(id);
    }

    private List<User> findByEmails(List<String> emails) {
        return emails.stream()
                     .map(userRepository::findByEmailOrElseThrow)
                     .toList();
    }

    private List<Member> createMembers(Workspace workspace, List<User> users) {
        return users.stream()
                    .map(user -> new Member(workspace, user, Part.READ))
                    .toList();
    }

    private List<MemberResponseDto> saveMemberAndConvertToDto(List<Member> members) {
        return members.stream()
                      .map(memberRepository::save)
                      .map(MemberResponseDto::toDto)
                      .toList();
    }
}
