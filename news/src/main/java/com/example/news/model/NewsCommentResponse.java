package com.example.news.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsCommentResponse {

    private Long id;
    private String text;
    private NewsResponse news;
    private UserShortResponse user;

}
