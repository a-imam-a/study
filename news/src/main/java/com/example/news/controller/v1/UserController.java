package com.example.news.controller.v1;

import com.example.news.entity.Role;
import com.example.news.entity.RoleType;
import com.example.news.entity.User;
import com.example.news.mapper.UserMapper;
import com.example.news.model.UserResponse;
import com.example.news.model.UserRequest;
import com.example.news.model.UserShortResponse;
import com.example.news.model.filter.UserFilter;
import com.example.news.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userServiceImpl;
    private final UserMapper userMapper;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserResponse>> findAll(@Valid UserFilter filter) {
        return ResponseEntity.ok(
                userServiceImpl.findAll(filter).stream()
                        .map(user -> userMapper.userToExtendedResponse(user))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                userMapper.userToExtendedResponse(userServiceImpl.findById(id))
        );
    }

    @PostMapping("/create")
    public ResponseEntity<UserShortResponse> createUser(@RequestBody @Valid UserRequest request, @RequestParam RoleType roleType) {
        User newUser = userServiceImpl.save(userMapper.requestToUser(request), Role.from(roleType));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.userToResponse(newUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateById(@PathVariable Long id, @RequestBody UserRequest request) {
        User updatedUser = userServiceImpl.update(userMapper.requestToUser(id, request));
        return ResponseEntity.ok(userMapper.userToExtendedResponse(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long clientId) {
        userServiceImpl.deleteById(clientId);
        return ResponseEntity.noContent().build();
    }
}
