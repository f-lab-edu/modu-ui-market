package com.flab.modu.admin.service;

import com.flab.modu.users.controller.UserDto.UserResponse;
import com.flab.modu.users.controller.UserDto.UserSearchCondition;
import com.flab.modu.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<UserResponse> findUsers(UserSearchCondition condition, Pageable pageable) {
        return userRepository.searchByUsers(condition, pageable);
    }
}
