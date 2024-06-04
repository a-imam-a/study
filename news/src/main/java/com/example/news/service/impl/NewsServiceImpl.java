package com.example.news.service.impl;

import com.example.news.entity.News;
import com.example.news.entity.User;
import com.example.news.exception.EntityNotFoundException;
import com.example.news.model.filter.NewsFilter;
import com.example.news.repository.NewsRepository;
import com.example.news.repository.specification.NewsSpecification;
import com.example.news.service.NewsService;
import com.example.news.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    @Override
    public List<News> findAll(NewsFilter filter) {
        return newsRepository.findAll(NewsSpecification.withFilter(filter),
                    PageRequest.of(filter.getPageNumber(), filter.getPageSize())
                ).getContent();
    }

    @Override
    public News findById(Long id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Новость по ID {0} не найдена!", id)));
    }

    @Override
    public News save(News news) {
        return newsRepository.save(news);
    }

    @Override
    public News update(News news) {
        News existedNews = findById(news.getId());
        BeanUtils.copyNonNullProperties(news, existedNews);
        return newsRepository.save(existedNews);    }

    @Override
    public void deleteById(Long id) {
        newsRepository.deleteById(id);
    }

    @Override
    public Long countByUser(User user) {
        return newsRepository.countByUser(user);
    }

    @Override
    public Long countNewsCommentById(Long id) {
        return newsRepository.countNewsCommentById(id);
    }
}
