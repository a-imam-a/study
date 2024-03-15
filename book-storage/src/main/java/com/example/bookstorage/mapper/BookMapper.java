package com.example.bookstorage.mapper;

import com.example.bookstorage.entity.Book;
import com.example.bookstorage.model.BookRequest;
import com.example.bookstorage.model.BookResponse;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@DecoratedWith(BookMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {

    Book requestToBook(BookRequest request);

    @Mapping(source = "id", target = "id")
    Book requestToBook(Long id, BookRequest request);

    BookResponse bookToResponse(Book book);

}
