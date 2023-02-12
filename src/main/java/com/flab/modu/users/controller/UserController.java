package com.flab.modu.users.controller;

import com.flab.modu.users.service.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserDto.CreateRequest createRequest) {
        userService.createUser(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
