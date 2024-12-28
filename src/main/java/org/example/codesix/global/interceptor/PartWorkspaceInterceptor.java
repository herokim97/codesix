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
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class PartWorkspaceInterceptor implements HandlerInterceptor {

    private final MemberRepository memberRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String method = request.getMethod();
        String path = request.getServletPath();
        AntPathMatcher pathMatcher = new AntPathMatcher();

        //워크스페이스 기능의 경우 GET, POST 기능 인가 X
        if ((pathMatcher.match("/api/workspaces/*", path) || pathMatcher.match("/api/workspaces", path)) && (method.equals("GET") || method.equals("POST"))) {
            return true;
        }

        //워크스프에시의 멤버 기능의 경우 GET 기능 인가 X
        if (pathMatcher.match("/api/workspaces/{workspacesId}/members/**", path) && method.equals("GET")) {
            return true;
        }

        //로그인한 유저의 정보
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loginEmail = auth.getName();

        //workspace Id 정보
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        if (pathVariables.containsKey("id")) {
            Long workspaceId = Long.valueOf(pathVariables.get("id"));
            Part part = memberRepository.findPartByUserEmailAndWorkspaceId(loginEmail, workspaceId);
            isWorkspace(part);
        } else if (pathVariables.containsKey("workspaceId")) {
            Long workspaceId = Long.valueOf(pathVariables.get("workspaceId"));
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
