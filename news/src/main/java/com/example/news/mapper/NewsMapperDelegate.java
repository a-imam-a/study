package com.example.news.mapper;

import com.example.news.entity.News;
import com.example.news.model.NewsRequest;
import com.example.news.model.NewsResponse;
import com.example.news.service.NewsCategoryService;
import com.example.news.service.NewsCommentService;
import com.example.news.service.NewsService;
import com.example.news.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class NewsMapperDelegate implements NewsMapper {

    @Autowired
    private NewsCommentService newsCommentService;
    @Autowired
    private NewsCategoryService newsCategoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private NewsCategoryMapper newsCategoryMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public News requestToNews(NewsRequest request, Long userId) {
        News news = new News();
        news.setNewsTitle(request.getNewsTitle());
        news.setNewsBody(request.getNewsBody());
        news.setCategory(newsCategoryService.findById(request.getCategoryId()));
        news.setUser(userService.findById(userId));
        return news;
    }

    @Override
    public News requestToNews(Long newsId, NewsRequest request, Long userId) {
        News news = requestToNews(request, userId);
        news.setId(newsId);
        return news;
    }

    @Override
    public NewsResponse newsToResponse(News news) {
        NewsResponse newsResponse = new NewsResponse();
        fillNewsResponse(newsResponse, news);
        newsResponse.setNumbersOfComment(newsCommentService.countByNewsId(news.getId()));
        return newsResponse;
    }

    private void fillNewsResponse(NewsResponse newsResponse, News news) {
        newsResponse.setId(news.getId());
        newsResponse.setNewsTitle(news.getNewsTitle());
        newsResponse.setNewsBody(news.getNewsBody());
        newsResponse.setCategory(newsCategoryMapper.newsCategoryToResponse(news.getCategory()));
        newsResponse.setUser(userMapper.userToResponse(news.getUser()));
    }
}
