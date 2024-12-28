package org.example.codesix.domain.workspace.repository;

import org.example.codesix.domain.workspace.entity.Member;
import org.example.codesix.domain.workspace.enums.Part;
import org.example.codesix.global.exception.ExceptionType;
import org.example.codesix.global.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(ExceptionType.MEMBER_NOT_FOUND));
    }

    List<Member> findAllByWorkspaceId(Long workspaceId);

    @Query("select m.part from Member m where m.user.email = :loginEmail and m.workspace.id = :workspaceId")
    Part findPartByUserEmailAndWorkspaceId(String loginEmail, Long workspaceId);

}
