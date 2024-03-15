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

    @Override
    @Cacheable(value = AppCacheProperties.CacheNames.ALL_BOOKS, key = "#filter.getPageNumber() + #filter.getPageSize()")
    public List<Book> findAll(BookFilter filter) {
        log.info("Calling BookServiceImpl.findAll");
        return repository.findAll(
                BookSpecification.withFilter(filter),
                PageRequest.of(filter.getPageNumber(), filter.getPageSize())
        ).getContent();
    }

    @Override
//    @Cacheable(value = AppCacheProperties.CacheNames.BOOK_BY_ID, key = "#id")
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
    @CacheEvict(value = AppCacheProperties.CacheNames.ALL_BOOKS, allEntries = true)
    public Book create(Book book) {
        saveBookCategory(book);
        return repository.save(book);
    }

    @Override
    @Caching(evict = {
/*            @CacheEvict(
                    value = AppCacheProperties.CacheNames.BOOK_BY_ID,
                    key = "#id",
                    beforeInvocation = true
            ),*/
            @CacheEvict(
                value = AppCacheProperties.CacheNames.BOOK_BY_NAME_AND_AUTHOR,
                key = "#book.getName() + #book.getAuthor()",
                beforeInvocation = true
    ),
            @CacheEvict(value = AppCacheProperties.CacheNames.ALL_BOOKS, allEntries = true)
    })
    public Book update(Long id, Book book) {
        saveBookCategory(book);
        Book existedBook = findById(id);
        BeanUtils.copyProperties(book, existedBook);
        return repository.save(existedBook);    }

    @Override
    @Caching(evict = {
/*            @CacheEvict(
                    value = AppCacheProperties.CacheNames.BOOK_BY_ID,
                    key = "#id",
                    beforeInvocation = true
            ),*/
            @CacheEvict(value = AppCacheProperties.CacheNames.BOOK_BY_NAME_AND_AUTHOR, allEntries = true),
            @CacheEvict(value = AppCacheProperties.CacheNames.ALL_BOOKS, allEntries = true)
    })
    public void deleteById(Long id) {
        repository.deleteById(id);
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
