package com.example.tasktracker.mapper;

import com.example.tasktracker.entity.Task;
import com.example.tasktracker.model.TaskRequest;
import com.example.tasktracker.model.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    TaskResponse taskToTaskResponse(Task task);

    Task taskRequestToTask(TaskRequest taskRequest);
}
