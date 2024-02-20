package com.example.news.controller.v1;

import com.example.news.entity.NewsComment;
import com.example.news.mapper.NewsCommentMapper;
import com.example.news.model.ErrorResponse;
import com.example.news.model.NewsCommentResponse;
import com.example.news.model.NewsCommentRequest;
import com.example.news.service.NewsCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;


@RestController
@RequestMapping("/api/v1/news-comment")
@RequiredArgsConstructor
@Slf4j
public class NewsCommentController {

    private final NewsCommentService newsCommentServiceImpl;

    private final NewsCommentMapper newsCommentMapper;

    @GetMapping
    public ResponseEntity<ErrorResponse> findAll() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ErrorResponse("Получение списка всех комментариев запрещено!!"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsCommentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                newsCommentMapper.newsCommentToResponse(newsCommentServiceImpl.findById(id))
        );
    }

    @PostMapping
    public ResponseEntity<NewsCommentResponse> create(@RequestHeader("User-Id") Long userId,
                                                      @RequestBody @Valid NewsCommentRequest request) {
        NewsComment newNewsComment = newsCommentServiceImpl.save(newsCommentMapper.requestToNewsComment(request, userId));
        return ResponseEntity.status(HttpStatus.CREATED).body(newsCommentMapper.newsCommentToResponse(newNewsComment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsCommentResponse> update(@RequestHeader("User-Id") Long userId,
                                                      @PathVariable("id") Long commentId, @RequestBody @Valid NewsCommentRequest request) {

        try {
            NewsComment updatedNewsComment= newsCommentServiceImpl.update(newsCommentMapper.requestToNewsComment(commentId, request, userId));
            return ResponseEntity.ok(
                    newsCommentMapper.newsCommentToResponse(updatedNewsComment)
            );
        }
        catch (HttpClientErrorException.Forbidden e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            newsCommentServiceImpl.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        catch (HttpClientErrorException.Forbidden e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
