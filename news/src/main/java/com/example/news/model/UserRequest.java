package com.example.news.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "Имя пользоватлеля должно быть заполнено!")
    private String username;

    @NotBlank(message = "Пароль пользоватлеля должен быть заполнен!")
    private String password;
}
