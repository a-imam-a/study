package com.example.news.mapper;

import com.example.news.entity.NewsCategory;
import com.example.news.model.NewsCategoryRequest;
import com.example.news.model.NewsCategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NewsCategoryMapper {

    NewsCategory requestToNewsCategory(NewsCategoryRequest request);

    @Mapping(source = "newsCategoryId", target = "id")
    NewsCategory requestToNewsCategory(Long newsCategoryId, NewsCategoryRequest request);

    NewsCategoryResponse newsCategoryToResponse(NewsCategory newsCategory);

}
