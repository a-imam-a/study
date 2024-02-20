package com.example.news.controller.v1;

import com.example.news.mapper.NewsMapper;
import com.example.news.model.filter.NewsFilter;
import com.example.news.model.NewsRequest;
import com.example.news.model.NewsResponse;
import com.example.news.model.NewsResponseWithComments;
import com.example.news.service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
@Slf4j
public class NewsController {

    private final NewsService newsServiceImpl;

    private final NewsMapper newsMapper;

    @GetMapping
    public ResponseEntity<List<NewsResponse>> findAll(@Valid NewsFilter filter) {
        System.out.println(filter.getPageNumber());
        return ResponseEntity.ok(
                newsServiceImpl.findAll(filter).stream()
                        .map(news -> newsMapper.newsToResponse(news))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsResponseWithComments> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                newsMapper.newsToResponseWithComments(newsServiceImpl.findById(id))
        );
    }

    @PostMapping
    public ResponseEntity<NewsResponseWithComments> create(@RequestHeader("User-Id") Long userId,
                                               @RequestBody @Valid NewsRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newsMapper.newsToResponseWithComments(
                        newsServiceImpl.save(
                                newsMapper.requestToNews(request, userId)
                        )
                    )
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsResponseWithComments> update(@RequestHeader("User-Id") Long userId,
                                               @PathVariable("id") Long newsId,
                                               @RequestBody @Valid NewsRequest request) {
        return ResponseEntity.ok(
                newsMapper.newsToResponseWithComments(
                        newsServiceImpl.update(
                                newsMapper.requestToNews(newsId, request, userId))
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        newsServiceImpl.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
