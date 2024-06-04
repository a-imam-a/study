package com.example.news.service.impl;

import com.example.news.entity.NewsComment;
import com.example.news.entity.User;
import com.example.news.exception.EntityNotFoundException;
import com.example.news.repository.NewsCommentRepository;
import com.example.news.service.NewsCommentService;
import com.example.news.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsCommentServiceImpl implements NewsCommentService {

    private final NewsCommentRepository repository;

    @Override
    public List<NewsComment> findAllByNewsId(Long newsId) {
        return repository.findAllByNewsId(newsId);
    }

    @Override
    public NewsComment findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Комментарий новости по ID {0} не найден!", id)));
    }

    @Override
    public NewsComment save(NewsComment newsComment) {
        return repository.save(newsComment);
    }

    @Override
    public NewsComment update(NewsComment newsComment) {
        NewsComment existedNewsComment = findById(newsComment.getId());
        BeanUtils.copyNonNullProperties(newsComment, existedNewsComment);
        return repository.save(existedNewsComment);    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Long countByNewsId(Long newsId) {
        return repository.countByNewsId(newsId);
    }

    @Override
    public Long countByUser(User user) {
        return repository.countByUser(user);
    }
}
