package com.example.news.controller.v1;

import com.example.news.entity.NewsCategory;
import com.example.news.mapper.NewsCategoryMapper;
import com.example.news.model.NewsCategoryRequest;
import com.example.news.model.NewsCategoryResponse;
import com.example.news.model.filter.NewsCategoryFilter;
import com.example.news.service.NewsCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/news-category")
@RequiredArgsConstructor
public class NewsCategoryController {

    private final NewsCategoryService newsCategoryServiceImpl;
    private final NewsCategoryMapper newsCategoryMapper;

    @GetMapping
    public ResponseEntity<List<NewsCategoryResponse>> findAll(@Valid NewsCategoryFilter filter) {
        return ResponseEntity.ok(
                newsCategoryServiceImpl.findAll(filter).stream()
                        .map(newsCategory -> newsCategoryMapper.newsCategoryToResponse(newsCategory))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsCategoryResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                newsCategoryMapper.newsCategoryToResponse(newsCategoryServiceImpl.findById(id))
        );
    }

    @PostMapping
    public ResponseEntity<NewsCategoryResponse> create(@RequestBody @Valid NewsCategoryRequest request) {
        NewsCategory newNewsCategory = newsCategoryServiceImpl.save(newsCategoryMapper.requestToNewsCategory(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(newsCategoryMapper.newsCategoryToResponse(newNewsCategory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsCategoryResponse> update(@PathVariable Long id, @RequestBody @Valid NewsCategoryRequest request) {
        NewsCategory updatedNewsCategory = newsCategoryServiceImpl.update(newsCategoryMapper.requestToNewsCategory(id, request));
        return ResponseEntity.ok(
                newsCategoryMapper.newsCategoryToResponse(updatedNewsCategory)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        newsCategoryServiceImpl.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
