package com.example.news.mapper;

import com.example.news.entity.NewsCategory;
import com.example.news.entity.NewsComment;
import com.example.news.model.NewsCommentRequest;
import com.example.news.service.NewsService;
import com.example.news.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class NewsCommentMapperDelegate implements NewsCommentMapper{

    @Autowired
    private UserService userService;

    @Autowired
    private NewsService newsService;

    @Override
    public NewsComment requestToNewsComment(NewsCommentRequest request, Long userId) {
        NewsComment newsComment = new NewsComment();
        newsComment.setText(request.getText());
        newsComment.setUser(userService.findById(userId));
        newsComment.setNews(newsService.findById(request.getNewsId()));
        return newsComment;
    }

    @Override
    public NewsComment requestToNewsComment(Long newsCommentId, NewsCommentRequest request, Long userId) {
        NewsComment newsComment = requestToNewsComment(request, userId);
        newsComment.setId(newsCommentId);
        return newsComment;
    }
}
