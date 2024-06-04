package com.example.news.aop;

import com.example.news.entity.News;
import com.example.news.entity.NewsComment;
import com.example.news.exception.ForbiddenException;
import com.example.news.security.AppUserPrincipal;
import com.example.news.service.NewsCommentService;
import com.example.news.service.NewsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.expression.AccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityAspect {

    private final NewsService newsService;
    private final NewsCommentService newsCommentService;

    @Before("execution(* com.example.news.controller.v1.UserController.findById(..)) || " +
            "execution(* com.example.news.controller.v1.UserController.updateById(..)) || " +
            "execution(* com.example.news.controller.v1.UserController.deleteById(..))")
    public void checkAccessBeforeUserController() throws AccessException {

        AppUserPrincipal userDetails = getUserDetails();
        if (!checkingAccessToResourceUser(userDetails, true)) {
            throw new ForbiddenException("Операция с пользователем запрещена!");
        }
    }

    @Before("execution(* com.example.news.controller.v1.NewsCategoryController.create(..)) || " +
            "execution(* com.example.news.controller.v1.NewsCategoryController.update(..)) || " +
            "execution(* com.example.news.controller.v1.NewsCategoryController.deleteById(..))")
    public void checkAccessBeforeNewsCategoryController() throws AccessException {

        AppUserPrincipal userDetails = getUserDetails();
        if (!checkingAuthorities(userDetails)) {
            throw new ForbiddenException("Операция с категорией новостей запрещена!");
        }
    }

    @Before("execution(* com.example.news.controller.v1.NewsController.update(..))")
    public void checkAccessBeforeNewsControllerUpdate() throws AccessException {

        AppUserPrincipal userDetails = getUserDetails();
        if (!checkingAccessToResourceNews(userDetails, false)) {
            throw new ForbiddenException("Запрещено менять новости других авторов!");
        }
    }

    @Before("execution(* com.example.news.controller.v1.NewsController.deleteById(..))")
    public void checkAccessBeforeNewsControllerDeleteById() throws AccessException {

        AppUserPrincipal userDetails = getUserDetails();
        if (!checkingAccessToResourceNews(userDetails, true)) {
            throw new ForbiddenException("Операция с новостью запрещена!");
        }
    }

    @Before("execution(* com.example.news.controller.v1.NewsCommentController.update(..))")
    public void checkAccessBeforeNewsCommentControllerUpdate() throws AccessException {

        AppUserPrincipal userDetails = getUserDetails();
        if (!checkingAccessToResourceNewsComment(userDetails, false)) {
            throw new ForbiddenException("Запрещено менять комментарии других пользователей!");
        }
    }

    @Before("execution(* com.example.news.controller.v1.NewsCommentController.deleteById(..))")
    public void checkAccessBeforeNewsCommentControllerDeleteById() throws AccessException {

        AppUserPrincipal userDetails = getUserDetails();
        if (!checkingAccessToResourceNewsComment(userDetails, true)) {
            throw new ForbiddenException("Операция с комментарием новости запрещена!");
        }
    }

    private boolean checkingAuthorities(AppUserPrincipal userDetails) {

        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_ADMIN") || a.equals("ROLE_MODERATOR"));

    }

    private boolean checkingAccessToResourceUser(AppUserPrincipal userDetails, boolean checkAuthorities) {

        if (checkAuthorities && checkingAuthorities(userDetails)) {
            return true;
        }

        boolean hasAccess = false;

        Map<String, String> pathVariables = pathVariables();
        if (userDetails.getId() == Long.parseLong(pathVariables.get("id"))) {
            hasAccess = true;
        }

        return hasAccess;
    }

    private boolean checkingAccessToResourceNews(AppUserPrincipal userDetails, boolean checkAuthorities) {

        if (checkAuthorities && checkingAuthorities(userDetails)) {
            return true;
        }

        boolean hasAccess = false;

        Map<String, String> pathVariables = pathVariables();
        Long newsId = Long.parseLong(pathVariables.get("id"));

        News news = newsService.findById(newsId);
        if (userDetails.getId() == news.getUser().getId()) {
            hasAccess = true;
        }

        return hasAccess;
    }

    private boolean checkingAccessToResourceNewsComment(AppUserPrincipal userDetails, boolean checkAuthorities) {

        if (checkAuthorities && checkingAuthorities(userDetails)) {
            return true;
        }

        boolean hasAccess = false;

        Map<String, String> pathVariables = pathVariables();
        Long newsId = Long.parseLong(pathVariables.get("id"));

        NewsComment newsComment = newsCommentService.findById(newsId);
        if (userDetails.getId() == newsComment.getUser().getId()) {
            hasAccess = true;
        }

        return hasAccess;
    }

    private Map<String, String> pathVariables() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        return (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    }

    private AppUserPrincipal getUserDetails() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AppUserPrincipal) {
            return  (AppUserPrincipal) authentication.getPrincipal();
        }

        throw new ForbiddenException("Access denied: insufficient permissions");

    }

}
