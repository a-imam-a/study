package com.example.bookstorage.service.impl;

import com.example.bookstorage.configuration.properties.AppCacheProperties;
import com.example.bookstorage.entity.Book;
import com.example.bookstorage.entity.Category;
import com.example.bookstorage.exception.EntityNotFoundException;
import com.example.bookstorage.model.filter.BookFilter;
import com.example.bookstorage.repository.BookRepository;
import com.example.bookstorage.repository.specification.BookSpecification;
import com.example.bookstorage.service.BookService;
import com.example.bookstorage.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository repository;

    private final CategoryService categoryServiceImpl;

    private final CacheManager cacheManager;


    @Override
    public List<Book> findAll(BookFilter filter) {
        log.info("Calling BookServiceImpl.findAll");
        return repository.findAll(
                BookSpecification.withFilter(filter),
                PageRequest.of(filter.getPageNumber(), filter.getPageSize())
        ).getContent();
    }

    @Override
    public Book findById(Long id) {
        log.info("Calling BookServiceImpl.findById");
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Книга по ID {0} не найдена!", id)));
    }

    @Override
    @Cacheable(value = AppCacheProperties.CacheNames.BOOK_BY_NAME_AND_AUTHOR, key = "#name + #author")
    public Book findBookByNameAndAuthor(String name, String author) {
        log.info("Calling BookServiceImpl.findBookByNameAndAuthor");
        return repository.findBookByNameAndAuthor(name, author)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                MessageFormat.format("Книга c названием {0} по автору {1} не найдена!", name, author))
                );
    }

    @Override
    public Book create(Book book) {
        saveBookCategory(book);
        return repository.save(book);
    }

    @Override
    public Book update(Long id, Book book) {

        EvictCacheByNameAndAuthor(book);

        saveBookCategory(book);
        Book existedBook = findById(id);
        BeanUtils.copyProperties(book, existedBook);
        return repository.save(existedBook);    }

    @Override
    public void deleteById(Long id) {
        Book book = findById(id);
        EvictCacheByNameAndAuthor(book);
        repository.deleteById(id);
    }

    private void EvictCacheByNameAndAuthor(Book book) {
        cacheManager
                .getCache(AppCacheProperties.CacheNames.BOOK_BY_NAME_AND_AUTHOR)
                .evict(book.getName() + book.getAuthor());
    }

    private void saveBookCategory(Book book) {
        Category category = book.getCategory();
        Category savedCategory = categoryServiceImpl.findByName(category.getName());
        if (savedCategory == null) {
            savedCategory =categoryServiceImpl.save(category);
        }
        book.setCategory(savedCategory);
    }

}
