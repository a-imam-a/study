package com.example.news.service.impl;

import com.example.news.entity.NewsCategory;
import com.example.news.entity.User;
import com.example.news.exception.EntityNotFoundException;
import com.example.news.model.filter.NewsCategoryFilter;
import com.example.news.repository.NewsCategoryRepository;
import com.example.news.service.NewsCategoryService;
import com.example.news.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsCategoryServiceImpl implements NewsCategoryService {

    private final NewsCategoryRepository repository;

    @Override
    public List<NewsCategory> findAll(NewsCategoryFilter filter) {
        return repository.findAll(
                PageRequest.of(filter.getPageNumber(), filter.getPageSize())
        ).getContent();
    }

    @Override
    public NewsCategory findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Категория новостей по ID {0} не найдена!", id)));
    }

    @Override
    public NewsCategory save(NewsCategory newsCategory) {
        return repository.save(newsCategory);
    }

    @Override
    public NewsCategory update(NewsCategory newsCategory) {
        NewsCategory existedNewsCategory = findById(newsCategory.getId());
        BeanUtils.copyNonNullProperties(newsCategory, existedNewsCategory);
        return repository.save(existedNewsCategory);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
