package com.example.bookstorage.model;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {

    @NotBlank(message = "Наименование книги должно быть заполнено!")
    private String name;

    @NotBlank(message = "Автор книги должен быть заполнен!")
    private String author;

    @NotBlank(message = "Категория книги должна быть заполнена!")
    private String categoryName;
}
