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
                                                                  "/api/workspaces/{workspacesId}/members/**",
                                                                  "/api/workspaces/{workspacesId}/notifications"};
    private static final String[] PART_BOARD_PATH_PATTERNS = {"/api/workspaces/{workspaceId}/**",
                                                                  "/api/workspaces/{workspacesId}/boards/{boardId}/**",
                                                                  "/api/workspaces/{workspacesId}/workLists/{workListId}/**",
                                                                  "/api/cards/{cardId}/**"};
//    private static final String[] WORKSPACE_PATH_PATTERNS = {"/api/workspaces/{workspaceId}/**"};
//    private static final String[] BOARD_PATH_PATTERNS = {"/api/workspaces/{workspacesId}/boards/{boardId}/**"};
//    private static final String[] WORKLIST_PATH_PATTERNS = {"/api/workspaces/{workspacesId}/workLists/{workListId}/**"};
//    private static final String[] CARD_PATH_PATTERNS = { "/api/cards/{cardId}/**"};

    private final PartWorkspaceInterceptor partWorkspaceInterceptor;
    private final PartBoardInterceptor partBoardInterceptor;

//    private final WorkspacePathInterceptor workspacePathInterceptor;
//    private final BoardPathInterceptor boardPathInterceptor;
//    private final WorkListPathInterceptor worklistPathInterceptor;
//    private final CardPathInterceptor cardPathInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(partWorkspaceInterceptor)
                .addPathPatterns(PART_WORKSPACE_PATH_PATTERNS)
                .excludePathPatterns(USER_PATH_PATTERNS)
                .order(Ordered.HIGHEST_PRECEDENCE);
        registry.addInterceptor(partBoardInterceptor)
                .addPathPatterns(PART_BOARD_PATH_PATTERNS)
                .excludePathPatterns(USER_PATH_PATTERNS)
                .excludePathPatterns(PART_WORKSPACE_PATH_PATTERNS)
                .order(Ordered.HIGHEST_PRECEDENCE + 1);
//        registry.addInterceptor(workspacePathInterceptor)
//                .addPathPatterns(WORKSPACE_PATH_PATTERNS)
//                .excludePathPatterns(USER_PATH_PATTERNS)
//                .excludePathPatterns(PART_WORKSPACE_PATH_PATTERNS)
//                .order(Ordered.HIGHEST_PRECEDENCE + 1);
//        registry.addInterceptor(boardPathInterceptor)
//                .addPathPatterns(BOARD_PATH_PATTERNS)
//                .excludePathPatterns(USER_PATH_PATTERNS)
//                .order(Ordered.HIGHEST_PRECEDENCE + 2);
//        registry.addInterceptor(worklistPathInterceptor)
//                .addPathPatterns(WORKLIST_PATH_PATTERNS)
//                .excludePathPatterns(USER_PATH_PATTERNS)
//                .order(Ordered.HIGHEST_PRECEDENCE + 3);
//        registry.addInterceptor(cardPathInterceptor)
//                .addPathPatterns(CARD_PATH_PATTERNS)
//                .excludePathPatterns(USER_PATH_PATTERNS)
//                .order(Ordered.HIGHEST_PRECEDENCE + 4);
    }
}
