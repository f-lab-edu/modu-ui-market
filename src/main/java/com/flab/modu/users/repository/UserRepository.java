package com.flab.modu.users.repository;

import com.flab.modu.users.controller.UserDto.UserResponse;
import com.flab.modu.users.controller.UserDto.UserSearchCondition;
import com.flab.modu.users.domain.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    boolean existsByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);

    void deleteByEmail(String email);
}
