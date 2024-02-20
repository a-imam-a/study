package com.example.news.model.filter;

import com.example.news.validation.FilterValid;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@FilterValid
public abstract class Filter {

    private Integer pageSize;
    private Integer pageNumber;

}
