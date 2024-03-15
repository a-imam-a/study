package com.example.bookstorage.mapper;

import com.example.bookstorage.entity.Book;
import com.example.bookstorage.entity.Category;
import com.example.bookstorage.model.BookRequest;
import com.example.bookstorage.model.BookResponse;

public abstract class BookMapperDelegate implements BookMapper {

    @Override
    public Book requestToBook(BookRequest request) {
        Book book = new Book();
        book.setName(request.getName());
        book.setAuthor(request.getAuthor());
        Category category = new Category();
        category.setName(request.getCategoryName());
        book.setCategory(category);
        return book;
    }

    @Override
    public Book requestToBook(Long id, BookRequest request) {
        Book book = requestToBook(request);
        book.setId(id);
        return book;
    }

    @Override
    public BookResponse bookToResponse(Book book) {
        BookResponse bookResponse = new BookResponse();
        bookResponse.setId(book.getId());
        bookResponse.setName(book.getName());
        bookResponse.setCategoryName(book.getCategory().getName());
        bookResponse.setAuthor(book.getAuthor());
        return bookResponse;
    }
}
