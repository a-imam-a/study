package com.example.bookstorage.repository;

import com.example.bookstorage.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    Page<Book> findAll(Pageable pageable);

    Optional<Book> findBookByNameAndAuthor(String name, String author);

}
