package com.example.bookstorage.service.impl;

import com.example.bookstorage.entity.Category;
import com.example.bookstorage.repository.CategoryRepository;
import com.example.bookstorage.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    @Override
    public Category findByName(String name) {
        return repository.findByName(name)
                .orElse(null);
    }

    @Override
    public Category save(Category category) {
        return repository.save(category);
    }
}
