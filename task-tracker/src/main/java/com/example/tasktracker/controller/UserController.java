package com.example.tasktracker.controller;

import com.example.tasktracker.entity.RoleType;
import com.example.tasktracker.mapper.UserMapper;
import com.example.tasktracker.model.UserModel;
import com.example.tasktracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('MANAGER', 'USER')")
    public Flux<UserModel> getAllUsers() {
        return userService.findAll().map(userMapper::userToModel);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'USER')")
    public Mono<ResponseEntity<UserModel>> getUserById(@PathVariable String id) {
        return userService.findById(id)
                .map(userMapper::userToModel)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<UserModel>> createUser(@RequestBody UserModel userModel,
                                                      @RequestParam RoleType roleType) {
        return userService.save(userMapper.modelToUser(userModel), roleType)
                .map(userMapper::userToModel)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'USER')")
    public Mono<ResponseEntity<UserModel>> updateUser(@PathVariable String id, @RequestBody UserModel userModel) {
        return userService.update(id, userMapper.modelToUser(userModel))
                .map(userMapper::userToModel)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'USER')")
    public Mono<ResponseEntity<Void>> deleteItem(@PathVariable String id) {
        return userService.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
    }

}
