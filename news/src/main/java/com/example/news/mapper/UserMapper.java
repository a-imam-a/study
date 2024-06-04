package com.example.news.mapper;

import com.example.news.entity.RoleType;
import com.example.news.entity.User;
import com.example.news.model.UserResponse;
import com.example.news.model.UserRequest;
import com.example.news.model.UserShortResponse;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@DecoratedWith(UserMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User requestToUser(UserRequest request);

    @Mapping(source = "userId", target = "id")
    User requestToUser(Long userId, UserRequest request);

    UserResponse userToExtendedResponse(User user);

    UserShortResponse userToResponse(User user);

}
