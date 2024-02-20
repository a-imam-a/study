package com.example.news.service;


import com.example.news.entity.NewsComment;
import com.example.news.entity.User;

import java.util.List;

public interface NewsCommentService {

    List<NewsComment> findAllByNewsId(Long newsId);

    NewsComment findById(Long id);

    NewsComment save(NewsComment newsComment);

    NewsComment update(NewsComment newsComment);

    void deleteById(Long id);
    Long countByNewsId(Long newsId);

    Long countByUser(User user);
}
