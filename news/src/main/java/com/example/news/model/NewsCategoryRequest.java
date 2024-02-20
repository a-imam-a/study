package com.example.news.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsCategoryRequest {

    @NotBlank(message = "Имя категории должно быть заполнено!")
    private String name;
}
