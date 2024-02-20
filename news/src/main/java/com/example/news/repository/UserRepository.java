package com.example.news.repository;

import com.example.news.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User, Long> {

    @Override
    @EntityGraph(attributePaths = {"newsList", "newsComments"})
    Page<User> findAll(Pageable pageable);
}
