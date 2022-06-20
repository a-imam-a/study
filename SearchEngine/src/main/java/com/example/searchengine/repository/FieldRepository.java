package com.example.searchengine.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.example.searchengine.entity.Field;

public interface FieldRepository extends CrudRepository<Field, Integer> {

}
