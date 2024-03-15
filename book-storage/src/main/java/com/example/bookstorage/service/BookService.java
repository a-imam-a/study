package com.example.bookstorage.service;

import com.example.bookstorage.entity.Book;
import com.example.bookstorage.model.filter.BookFilter;

import java.util.List;

public interface BookService {

    List<Book> findAll(BookFilter filter);

    Book findById(Long id);

    Book findBookByNameAndAuthor(String name, String author);
    Book create(Book book);

    Book update(Long id, Book book);

    void deleteById(Long id);
}
