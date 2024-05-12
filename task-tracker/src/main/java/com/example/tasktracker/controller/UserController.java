package com.example.tasktracker.controller;

import com.example.tasktracker.mapper.UserMapper;
import com.example.tasktracker.model.UserModel;
import com.example.tasktracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @GetMapping
    public Flux<UserModel> getAllItems() {
        return userService.findAll().map(userMapper::userToModel);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserModel>> getItemById(@PathVariable String id) {
        return userService.findById(id)
                .map(userMapper::userToModel)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<UserModel>> createUser(@RequestBody UserModel userModel) {
        return userService.save(userMapper.modelToUser(userModel))
                .map(userMapper::userToModel)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserModel>> updateUser(@PathVariable String id, @RequestBody UserModel userModel) {
        return userService.update(id, userMapper.modelToUser(userModel))
                .map(userMapper::userToModel)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteItem(@PathVariable String id) {
        return userService.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
    }

}
