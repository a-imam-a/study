package com.example.news.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponseWithComments {

    private Long id;
    private String newsTitle;
    private String newsBody;
    private NewsCategoryResponse category;
    private UserShortResponse user;
    private List<NewsCommentResponseWithoutNews> newsComments = new ArrayList<>();
}
