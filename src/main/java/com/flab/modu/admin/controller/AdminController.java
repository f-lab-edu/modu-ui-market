package com.flab.modu.admin.controller;

import com.flab.modu.admin.service.AdminService;
import com.flab.modu.users.controller.UserDto.UserResponse;
import com.flab.modu.users.controller.UserDto.UserSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/admin")
@RestController
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public Page<UserResponse> findByUsers(UserSearchCondition condition, Pageable pageable) {
        return adminService.findUsers(condition, pageable);
    }
}
