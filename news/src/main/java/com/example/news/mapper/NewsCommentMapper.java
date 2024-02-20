package com.example.news.mapper;

import com.example.news.entity.NewsComment;
import com.example.news.model.NewsCommentRequest;
import com.example.news.model.NewsCommentResponse;
import com.example.news.model.NewsCommentResponseWithoutNews;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@DecoratedWith(NewsCommentMapperDelegate.class)
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NewsCommentMapper {

    NewsComment requestToNewsComment(NewsCommentRequest request, Long userId);

    @Mapping(source = "newsCommentId", target = "id")
    NewsComment requestToNewsComment(Long newsCommentId, NewsCommentRequest request, Long userId);

    NewsCommentResponse newsCommentToResponse(NewsComment newsComment);

    NewsCommentResponseWithoutNews newsCommentToResponseWithoutNews(NewsComment newsComment);

}
