package com.example.news.service;

import com.example.news.entity.NewsCategory;
import com.example.news.model.filter.NewsCategoryFilter;

import java.util.List;

public interface NewsCategoryService {

    List<NewsCategory> findAll(NewsCategoryFilter filter);

    NewsCategory findById(Long id);

    NewsCategory save(NewsCategory newsCategory);

    NewsCategory update(NewsCategory NewsCategory);

    void deleteById(Long id);
}
