package com.example.news.service;

import com.example.news.entity.News;
import com.example.news.entity.User;
import com.example.news.model.filter.NewsFilter;

import java.util.List;

public interface NewsService {

    List<News> findAll(NewsFilter filter);

    News findById(Long id);

    News save(News news);

    News update(News news);

    void deleteById(Long id);

    Long countByUser(User user);

    Long countNewsCommentById(Long id);

}
