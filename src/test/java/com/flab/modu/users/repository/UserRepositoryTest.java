package com.flab.modu.users.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.modu.global.config.JpaConfig;
import com.flab.modu.users.domain.common.UserRole;
import com.flab.modu.users.domain.entity.User;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("JPA 연결 테스트")
@ActiveProfiles("test")
@Import(JpaConfig.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    private void insertTestData() {
        User user = User.builder()
            .email("test@modu.com")
            .name("test")
            .password("testPwd1234")
            .role(UserRole.BUYER)
            .build();
        User savedUser = userRepository.save(user);
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
            .hasSize(1);
    }
}
