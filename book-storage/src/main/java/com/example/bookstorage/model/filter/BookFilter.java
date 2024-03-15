package com.example.bookstorage.model.filter;

import com.example.bookstorage.validation.FilterValid;
import lombok.Data;

@FilterValid
@Data
public class BookFilter extends Filter{
    private String categoryName;
}
