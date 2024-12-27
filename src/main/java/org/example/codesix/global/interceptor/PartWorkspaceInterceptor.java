package org.example.codesix.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.codesix.domain.workspace.enums.Part;
import org.example.codesix.domain.workspace.repository.MemberRepository;
import org.example.codesix.global.exception.ExceptionType;
import org.example.codesix.global.exception.ForbiddenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class PartWorkspaceInterceptor implements HandlerInterceptor {

    private final MemberRepository memberRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (request.getMethod().equals("GET") || request.getMethod().equals("POST")) {
            return true;
        }

        //로그인한 유저의 정보
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loginEmail = auth.getName();

        //workspace Id 정보
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        if (pathVariables.containsKey("id")) {
            String workspaceId = pathVariables.get("id");
            Part part = memberRepository.findPartByUserEmailAndWorkspaceId(loginEmail, workspaceId);
            isWorkspace(part);
        } else if (pathVariables.containsKey("workspaceId")) {
            String workspaceId = pathVariables.get("workspaceId");
            Part part = memberRepository.findPartByUserEmailAndWorkspaceId(loginEmail, workspaceId);
            isWorkspace(part);
        }
        return true;
    }

    public static void isWorkspace(Part part) {
        if (part != Part.WORKSPACE) {
            throw new ForbiddenException(ExceptionType.FORBIDDEN_ACTION);
        }
    }
}
