package com.example.bookstorage.model.filter;

import com.example.bookstorage.validation.FilterValid;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@FilterValid
public abstract class Filter {
    private Integer pageSize;
    private Integer pageNumber;
}
