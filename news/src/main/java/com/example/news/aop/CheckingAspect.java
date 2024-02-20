package com.example.news.aop;

import com.example.news.entity.News;
import com.example.news.entity.NewsComment;
import com.example.news.exception.ForbiddenException;
import com.example.news.service.NewsCommentService;
import com.example.news.service.NewsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.AccessException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Aspect
@Component
@Slf4j
public class CheckingAspect {

    @Autowired
    private NewsService newsService;

    @Autowired
    private NewsCommentService newsCommentService;

    @Before("@annotation(Checkable)")
    public void checkBefore(JoinPoint joinPoint) throws AccessException {

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        Long userId = Long.valueOf(request.getHeader("User-id"));
        var pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
        if (declaringTypeName.contains("NewsServiceImpl")) {
            Long newsId = Long.parseLong(pathVariables.get("id"));
            News news = newsService.findById(newsId);
            if (news.getUser().getId() != userId) {
                throw new ForbiddenException("Запрещено менять новости других авторов!!");
            }
        }
        if (declaringTypeName.contains("NewsCommentServiceImpl")) {
            Long newsCommentId = Long.parseLong(pathVariables.get("id"));
            NewsComment newsComment = newsCommentService.findById(newsCommentId);
            if (newsComment.getUser().getId() != userId) {
                throw new ForbiddenException("Запрещено менять комментарии других пользователей!!");
            }
        }


    }
}
