package com.example.bookstorage.repository.specification;

import com.example.bookstorage.entity.Book;
import com.example.bookstorage.model.filter.BookFilter;
import org.springframework.data.jpa.domain.Specification;

public interface BookSpecification {

    static Specification<Book> withFilter(BookFilter filter) {
        return Specification.where(byCategoryName(filter.getCategoryName()));
    }

    static Specification<Book> byCategoryName(String categoryName) {
        return (root, query, criteriaBuilder) -> {
            if (categoryName == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("category").get("name"), categoryName);
        };
    }
}
