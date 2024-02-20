package com.example.news.mapper;

import com.example.news.entity.News;
import com.example.news.model.NewsRequest;
import com.example.news.model.NewsResponse;
import com.example.news.model.NewsResponseWithComments;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@DecoratedWith(NewsMapperDelegate.class)
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NewsMapper {

    News requestToNews(NewsRequest request, Long userId);

    @Mapping(source = "newsId", target = "id")
    News requestToNews(Long newsId, NewsRequest request, Long userId);

    NewsResponse newsToResponse(News news);

    NewsResponseWithComments newsToResponseWithComments(News news);

}
