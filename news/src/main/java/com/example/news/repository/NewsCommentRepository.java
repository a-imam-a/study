package com.example.news.repository;

import com.example.news.entity.NewsComment;
import com.example.news.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsCommentRepository extends JpaRepository<NewsComment, Long> {

    List<NewsComment> findAllByNewsId(Long newsId);

    Long countByNewsId(Long newsId);

    Long countByUser(User user);
}
