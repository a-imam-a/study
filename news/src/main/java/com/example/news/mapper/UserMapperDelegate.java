package com.example.news.mapper;

import com.example.news.entity.RoleType;
import com.example.news.entity.User;
import com.example.news.model.UserRequest;
import com.example.news.model.UserResponse;
import com.example.news.model.UserShortResponse;
import com.example.news.service.NewsCommentService;
import com.example.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class UserMapperDelegate implements UserMapper {

    @Autowired
    private NewsService newsService;

    @Autowired
    private NewsCommentService newsCommentService;


    @Override
    public UserResponse userToExtendedResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setPassword(user.getPassword());
        userResponse.setNumbersOfNews(newsService.countByUser(user));
        userResponse.setNumbersOfComment(newsCommentService.countByUser(user));
        return userResponse;
    }

    @Override
    public UserShortResponse userToResponse(User user) {
        UserShortResponse userShortResponse = new UserShortResponse();
        userShortResponse.setId(user.getId());
        userShortResponse.setUsername(user.getUsername());
        userShortResponse.setPassword(user.getPassword());
        return userShortResponse;
    }
}
