package com.example.news.validation;

import com.example.news.model.filter.Filter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class FilterValidValidator implements ConstraintValidator<FilterValid, Filter> {
    @Override
    public boolean isValid(Filter value, ConstraintValidatorContext context) {
        if (value.getPageNumber() == null || value.getPageSize() == null) {
            return false;
        }
        return true;
    }
}
