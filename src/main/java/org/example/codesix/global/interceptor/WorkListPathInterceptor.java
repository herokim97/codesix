package org.example.codesix.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.codesix.domain.worklist.repository.WorkListRepository;
import org.example.codesix.domain.workspace.enums.Part;
import org.example.codesix.domain.workspace.repository.MemberRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

import static org.example.codesix.global.interceptor.PartBoardInterceptor.isBoardOrWorkspace;

@RequiredArgsConstructor
@Component
public class WorkListPathInterceptor implements HandlerInterceptor {

    private final MemberRepository memberRepository;
    private final WorkListRepository workListRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (request.getMethod().equals("GET")) {
            return true;
        }

        //로그인한 유저의 정보
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loginEmail = auth.getName();

        //workspace 정보
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        if (pathVariables.containsKey("WorkListId")) {
            Long workListId = Long.valueOf(pathVariables.get("workListId"));
            Long workspaceId = workListRepository.findWorkspaceIdById(workListId);
            Part part = memberRepository.findPartByUserEmailAndWorkspaceId(loginEmail, workspaceId);
            isBoardOrWorkspace(part);
        }
        return true;
    }
}
