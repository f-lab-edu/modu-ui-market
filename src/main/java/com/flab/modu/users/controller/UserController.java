package com.flab.modu.users.controller;

import com.flab.modu.global.annotation.CurrentUser;
import com.flab.modu.global.annotation.LoginCheck;
import com.flab.modu.users.controller.UserDto.LoginRequest;
import com.flab.modu.users.service.LoginService;
import com.flab.modu.users.service.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    private final LoginService loginService;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody @Valid UserDto.CreateRequest createRequest) {
        userService.createUser(createRequest);
    }

    @LoginCheck
    @DeleteMapping("/users")
    public void userWithdrawal(@RequestBody @Valid UserDto.PasswordRequest requestDto,
        @CurrentUser String email) {
        String password = requestDto.getPassword();
        userService.delete(email, password);
        loginService.logout();
    }

    @PostMapping("/users/login")
    public void login(@RequestBody @Valid UserDto.LoginRequest loginRequest) {
        loginService.login(loginRequest);
    }

    @LoginCheck
    @DeleteMapping("/users/logout")
    public void logout() {
        loginService.logout();
    }

    @GetMapping("/users/session")
    public String getUserInfo(LoginRequest loginRequest) {
        return loginService.getLoginUser();
    }
}
