package com.example.bookstorage.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FilterValidValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FilterValid {

    String message() default "Поля пагинации (pageNumber и pageSize) должны быть указаны!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
