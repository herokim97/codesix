package org.example.codesix.domain.user.repository;

import org.example.codesix.domain.user.entity.User;
import org.example.codesix.domain.workspace.entity.Workspace;
import org.example.codesix.global.exception.ExceptionType;
import org.example.codesix.global.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    default User findByEmailOrElseThrow (String email) {
        return findByEmail(email).orElseThrow(() -> new NotFoundException(ExceptionType.USER_NOT_FOUND));
    }

    default User findByIdOrElseThrow (Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(ExceptionType.USER_NOT_FOUND));
    }
}
