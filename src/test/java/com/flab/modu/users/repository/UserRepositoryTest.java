package com.flab.modu.users.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.modu.global.config.JpaConfig;
import com.flab.modu.users.controller.UserDto.UserResponse;
import com.flab.modu.users.controller.UserDto.UserSearchCondition;
import com.flab.modu.users.domain.common.UserRole;
import com.flab.modu.users.domain.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private final String EMAIL = "test@modu.com";

    private final int TEST_SIZE = 10;

    @BeforeEach
    private void insertTestData() {
        List<User> users = IntStream.range(0, 10)
            .mapToObj(i -> createUser(i))
            .collect(Collectors.toList());

        userRepository.saveAll(users);
    }

    @Test
    @DisplayName("db 연결 테스트")
    public void selectUser() throws Exception {
        // given

        // when
        List<User> users = userRepository.findAll();

        // then
        assertThat(users)
            .isNotNull()
            .hasSize(TEST_SIZE);
    }

    @Test
    @DisplayName("querydsl 테스트")
    public void searchUser_useQueryDsl() throws Exception {
        // given
        UserSearchCondition allCondition = UserSearchCondition.builder()
            .build();

        String findEmail = getConcatData(EMAIL, 0);
        UserSearchCondition emailCondition = UserSearchCondition.builder()
            .email(findEmail)
            .build();

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<UserResponse> allUsers = userRepository.searchByUsers(allCondition, pageable);
        Page<UserResponse> user = userRepository.searchByUsers(emailCondition, pageable);
        UserResponse userResponse = user.stream().findFirst().get();

        // then
        assertThat(allUsers.getTotalElements()).isEqualTo(TEST_SIZE);
        assertThat(user.getTotalElements()).isEqualTo(1);
        assertThat(userResponse.getEmail()).isEqualTo(findEmail);
    }

    private User createUser(int i) {
        return User.builder()
            .email(getConcatData(EMAIL, i))
            .name(getConcatData("test", i))
            .password(getConcatData("testPwd1234", i))
            .role(UserRole.BUYER)
            .build();
    }

    private String getConcatData(String data, int i) {
        return new StringBuilder(data).append(i).toString();
    }
}
