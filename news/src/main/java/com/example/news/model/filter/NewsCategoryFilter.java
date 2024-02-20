package com.example.news.model.filter;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewsCategoryFilter{
    private Integer pageSize;
    private Integer pageNumber;
}
