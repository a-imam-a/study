package com.example.news.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponse {

    private Long id;
    private String newsTitle;
    private String newsBody;
    private NewsCategoryResponse category;
    private UserShortResponse user;
    private Long numbersOfComment = 0L;

}
