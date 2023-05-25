package com.flab.modu.users.repository;

import com.flab.modu.users.controller.UserDto.UserResponse;
import com.flab.modu.users.controller.UserDto.UserSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {

    Page<UserResponse> searchByUsers(UserSearchCondition condition, Pageable pageable);

}
