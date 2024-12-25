package org.example.codesix.domain.workspace.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.codesix.domain.user.entity.User;
import org.example.codesix.domain.user.repository.UserRepository;
import org.example.codesix.domain.workspace.dto.WorkspaceRequestDto;
import org.example.codesix.domain.workspace.dto.WorkspaceResponseDto;
import org.example.codesix.domain.workspace.entity.Workspace;
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

    public void deleteWorkspace(Long id) {
        Workspace workspace = findById(id);
        workspaceRepository.delete(workspace);
    }

    public Workspace findById(Long id) {
        return workspaceRepository.findByIdOrElseThrow(id);
    }
}
