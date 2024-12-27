package org.example.codesix.global.config;

import lombok.RequiredArgsConstructor;
import org.example.codesix.global.interceptor.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private static final String[] USER_PATH_PATTERNS = {"/api/users/**"};
    private static final String[] PART_WORKSPACE_PATH_PATTERNS = {"/api/workspaces/*",
                                                                  "/api/workspaces",
                                                                  "/api/workspaces/{workspacesId}/members/**"};
    private static final String[] WORKSPACE_PATH_PATTERNS = { "/api/workspaces/{workspaceId}/**"};
    private static final String[] BOARD_PATH_PATTERNS = { "/api/boards/{boardId}/**"};
    private static final String[] WORKLIST_PATH_PATTERNS = { "/api/workLists/{workListId}/**"};
    private static final String[] CARD_PATH_PATTERNS = { "/api/cards/{cardId}/**"};

    private final PartWorkspaceInterceptor partWorkspaceInterceptor;
    private final WorkspacePathInterceptor workspacePathInterceptor;
    private final BoardPathInterceptor boardPathInterceptor;
    private final WorkListPathInterceptor worklistPathInterceptor;
    private final CardPathInterceptor cardPathInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(partWorkspaceInterceptor)
                .addPathPatterns(PART_WORKSPACE_PATH_PATTERNS)
                .excludePathPatterns(USER_PATH_PATTERNS)
                .order(Ordered.HIGHEST_PRECEDENCE);
        registry.addInterceptor(workspacePathInterceptor)
                .addPathPatterns(WORKSPACE_PATH_PATTERNS)
                .excludePathPatterns(USER_PATH_PATTERNS)
                .excludePathPatterns(PART_WORKSPACE_PATH_PATTERNS)
                .order(Ordered.HIGHEST_PRECEDENCE + 1);
        registry.addInterceptor(boardPathInterceptor)
                .addPathPatterns(BOARD_PATH_PATTERNS)
                .excludePathPatterns(USER_PATH_PATTERNS)
                .order(Ordered.HIGHEST_PRECEDENCE + 2);
        registry.addInterceptor(worklistPathInterceptor)
                .addPathPatterns(WORKLIST_PATH_PATTERNS)
                .excludePathPatterns(USER_PATH_PATTERNS)
                .order(Ordered.HIGHEST_PRECEDENCE + 3);
        registry.addInterceptor(cardPathInterceptor)
                .addPathPatterns(CARD_PATH_PATTERNS)
                .excludePathPatterns(USER_PATH_PATTERNS)
                .order(Ordered.HIGHEST_PRECEDENCE + 4);
    }
}
