package com.example.bookstorage.service;

import com.example.bookstorage.entity.Category;

public interface CategoryService {

    Category findByName(String name);

    Category save(Category category);
}
