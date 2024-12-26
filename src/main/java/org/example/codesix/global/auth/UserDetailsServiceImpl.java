package org.example.codesix.global.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.codesix.domain.user.entity.User;
import org.example.codesix.domain.user.enums.UserRole;
import org.example.codesix.domain.user.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//사용자 정보 기술 커스터마이징
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findUserByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("사용자를 찾을 수 없다."));

        return new UserDetailsImpl(user);
    }
}
