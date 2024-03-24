package com.example.bookstorage.controller;

import com.example.bookstorage.AbstractTest;
import com.example.bookstorage.entity.Book;
import com.example.bookstorage.entity.Category;
import com.example.bookstorage.model.BookRequest;
import com.example.bookstorage.model.BookResponse;
import com.example.bookstorage.model.filter.BookFilter;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookController extends AbstractTest {

    @Test
    public void whenGetAllBooks_thenReturnBookList() throws Exception {
        assertTrue(redisTemplate.keys("*").isEmpty());
        String actualResponse = mockMvc.perform(get("/api/v1/book?pageNumber=0&pageSize=5"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        BookFilter bookFilter = new BookFilter();
        bookFilter.setPageNumber(0);
        bookFilter.setPageSize(5);

        String expectedResponse = objectMapper.writeValueAsString(bookService.findAll(bookFilter).stream()
                .map(book -> bookMapper.bookToResponse(book))
                .collect(Collectors.toList()));

        assertTrue(redisTemplate.keys("*").isEmpty());
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenGetBookByNameAndAndAuthor_thenReturnOneBook() throws Exception {

        assertTrue(redisTemplate.keys("*").isEmpty());

        String actualResponse = getBook1Author1();

        String expectedResponse = objectMapper.writeValueAsString(
                bookMapper.bookToResponse(
                        bookService.findBookByNameAndAuthor("Book1", "Author1")
                )
        );

        assertFalse(redisTemplate.keys("*").isEmpty());
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenCreateBook_thenCreateBook() throws Exception {

        assertTrue(redisTemplate.keys("*").isEmpty());
        assertEquals(3, bookRepository.count());

        getBook1Author1();

        assertFalse(redisTemplate.keys("*").isEmpty());

        BookRequest request = new BookRequest();
        request.setName("newBook");
        request.setAuthor("newAuthor");
        request.setCategoryName("Category1");

        String actualResponse = mockMvc.perform(post("/api/v1/book")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        BookResponse bookResponse = new BookResponse(1234L, "newBook", "newAuthor", "Category1");
        String expectedResponse = objectMapper.writeValueAsString(bookResponse);

        assertFalse(redisTemplate.keys("*").isEmpty());
        assertEquals(4, bookRepository.count());
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse, JsonAssert.whenIgnoringPaths("id"));
    }

    @Test
    public void whenUpdateBook_thenUpdateBookAndEvictCache() throws Exception {

        assertTrue(redisTemplate.keys("*").isEmpty());
        assertEquals(3, bookRepository.count());

        getBook1Author1();

        assertFalse(redisTemplate.keys("*").isEmpty());

        BookRequest request = new BookRequest();
        request.setName("Book1");
        request.setAuthor("Author1");
        request.setCategoryName("Category2");

        String actualResponse = mockMvc.perform(put("/api/v1/book/{id}", UPDATED_ID.toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        BookResponse bookResponse = new BookResponse(UPDATED_ID, "Book1", "Author1", "Category2");
        String expectedResponse = objectMapper.writeValueAsString(bookResponse);

        assertTrue(redisTemplate.keys("*").isEmpty());
        assertEquals(3, bookRepository.count());
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenDeleteEntityById_thenDeleteEntityByIdAndEvictCache() throws Exception {
        assertTrue(redisTemplate.keys("*").isEmpty());
        assertEquals(3, bookRepository.count());

        getBook1Author1();

        assertFalse(redisTemplate.keys("*").isEmpty());
        mockMvc.perform(delete("/api/v1/book/" + UPDATED_ID))
                .andExpect(status().isNoContent());
        assertTrue(redisTemplate.keys("*").isEmpty());
        assertEquals(2, bookRepository.count());

    }

    private String getBook1Author1() throws Exception {
        return mockMvc.perform(get("/api/v1/book/?name=Book1&author=Author1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}
