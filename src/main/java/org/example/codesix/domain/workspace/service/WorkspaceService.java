package org.example.codesix.domain.workspace.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.codesix.domain.notification.enums.Type;
import org.example.codesix.domain.notification.repository.NotificationRepository;
import org.example.codesix.domain.notification.service.SlackService;
import org.example.codesix.domain.user.entity.User;
import org.example.codesix.domain.user.repository.UserRepository;
import org.example.codesix.domain.workspace.dto.*;
import org.example.codesix.domain.workspace.entity.Member;
import org.example.codesix.domain.workspace.entity.Workspace;
import org.example.codesix.domain.workspace.enums.Part;
import org.example.codesix.domain.workspace.repository.MemberRepository;
import org.example.codesix.domain.workspace.repository.WorkspaceRepository;
import org.example.codesix.global.exception.BadValueException;
import org.example.codesix.global.exception.ExceptionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final SlackService slackService;

    public WorkspaceResponseDto createWorkspace(Long userId, WorkspaceRequestDto requestDto) {

        User creator = userRepository.findByIdOrElseThrow(userId);

        Workspace workspace = new Workspace(requestDto.getTitle(), requestDto.getDescription(), creator);
        Workspace savedWorkspace = workspaceRepository.save(workspace);

        Member member = new Member(workspace, creator,Part.WORKSPACE);

        memberRepository.save(member);

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
    public void updateNotificationSettings(Long id, WorkspaceNotificationRequestDto requestDto) {
        Workspace workspace = findById(id);
        workspace.updateNotificationSettings(requestDto.getOAuthToken(), requestDto.getNotificationChannel());
    }

    @Transactional
    public void deleteWorkspace(Long id) {
        workspaceRepository.findByIdOrElseThrow(id);
        workspaceRepository.deleteById(id);
    }

    @Transactional
    public List<MemberResponseDto> addMember(Long workspaceId, MemberRequestDto memberRequestDto) {
        Workspace workspace = findById(workspaceId);
        List<User> users = findByEmails(memberRequestDto.getEmails());

        List<Long> userIds = users.stream().map(User::getId).toList();
        userIds.forEach(userId -> memberRepository.ifExistsByUserIdAndWorkspaceThenThrow(userId, workspace));

        List<Member> members = createMembers(workspace,users);

        slackService.callSlackApi(workspace.getTitle(), "", Type.MEMBER_ADD, workspace);
        return saveMemberAndConvertToDto(members);
    }

    @Transactional
    public MemberResponseDto updateMemberPart(Long memberId, MemberPartRequestDto memberPartRequestDto) {
        Member member = memberRepository.findByIdOrElseThrow(memberId);
        member.updatePart(memberPartRequestDto.getPart());
        return MemberResponseDto.toDto(member);
    }

    public List<MemberResponseDto> findAllMembers(Long workspaceId) {
        List<Member> members = memberRepository.findAllByWorkspaceId(workspaceId);
        return members.stream().map(MemberResponseDto::toDto).toList();
    }

    @Transactional
    public void deleteMember(Long workspaceId, Long memberId) {
        Workspace workspace = findById(workspaceId);
        Member member = memberRepository.findByIdOrElseThrow(memberId);
        memberRepository.delete(member);
        slackService.callSlackApi(workspace.getTitle(), "", Type.MEMBER_DELETE, workspace);
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
