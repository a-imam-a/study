package com.example.searchengine.service;

import com.example.searchengine.entity.Field;
import com.example.searchengine.repository.FieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FieldService {

    @Autowired
    FieldRepository fieldRepository;

    public ArrayList<Field> getFields() {
        Iterable<Field> fieldIterable = fieldRepository.findAll();
        ArrayList<Field> fieldList = StreamSupport.stream(
                fieldIterable.spliterator(), false)
                .collect(Collectors.toCollection(ArrayList::new));
        return fieldList;
    }

    public void deleteAll() {
        fieldRepository.deleteAll();
    }


}
