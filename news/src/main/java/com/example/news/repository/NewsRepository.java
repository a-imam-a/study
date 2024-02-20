package com.example.news.repository;

import com.example.news.entity.News;
import com.example.news.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface NewsRepository  extends JpaRepository<News, Long>, JpaSpecificationExecutor<News> {

    Page<News> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"category", "user", "newsComments"})
    Optional<News> findById(Long id);
    Long countByUser(User user);

    Long countNewsCommentById(Long id);
}
