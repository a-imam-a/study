package com.example.bookstorage.controller.v1;

import com.example.bookstorage.entity.Book;
import com.example.bookstorage.mapper.BookMapper;
import com.example.bookstorage.model.BookRequest;
import com.example.bookstorage.model.BookResponse;
import com.example.bookstorage.model.filter.BookFilter;
import com.example.bookstorage.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookServiceImpl;

    private final BookMapper bookMapper;

    @GetMapping
    public ResponseEntity<List<BookResponse>> findAll(@Valid BookFilter filter) {
        return ResponseEntity.ok(
                bookServiceImpl.findAll(filter).stream()
                        .map(book -> bookMapper.bookToResponse(book))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/")
    public ResponseEntity<BookResponse> findBookByNameAndAndAuthor(@RequestParam String name, @RequestParam String author) {
        return ResponseEntity.ok(
                bookMapper.bookToResponse(
                        bookServiceImpl.findBookByNameAndAuthor(name, author)
                )
        );
    }

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@RequestBody @Valid BookRequest request) {
        Book newBook = bookServiceImpl.create(bookMapper.requestToBook(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(bookMapper.bookToResponse(newBook));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable Long id, @RequestBody @Valid BookRequest request) {
        Book updatedBook = bookServiceImpl.update(id, bookMapper.requestToBook(id, request));
        return ResponseEntity.ok(
                bookMapper.bookToResponse(updatedBook)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookServiceImpl.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
