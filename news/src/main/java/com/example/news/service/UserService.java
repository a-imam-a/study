package com.example.news.service;

import com.example.news.entity.User;
import com.example.news.model.filter.UserFilter;

import java.util.List;

public interface UserService {

    List<User> findAll(UserFilter filter);

    User findById(Long id);

    User save(User user);

    User update(User user);

    void deleteById(Long id);

}
