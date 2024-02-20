package com.example.news.model.filter;

import com.example.news.validation.FilterValid;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewsFilter extends Filter{
    private Long categoryId;
    private Long userId;
}
