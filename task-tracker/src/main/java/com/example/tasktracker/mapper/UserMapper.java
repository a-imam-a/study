package com.example.tasktracker.mapper;

import com.example.tasktracker.entity.User;
import com.example.tasktracker.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserModel userToModel(User user);

    User modelToUser(UserModel model);

}
