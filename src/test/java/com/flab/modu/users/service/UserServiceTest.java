package com.flab.modu.users.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.flab.modu.users.controller.UserDto;
import com.flab.modu.users.domain.common.UserRole;
import com.flab.modu.users.domain.entity.User;
import com.flab.modu.users.exception.DuplicatedEmailException;
import com.flab.modu.users.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("정상적으로 회원가입에 성공한다.")
    public void createUser_successful() {
        // given
        UserDto.CreateRequest createRequest = createTestUserData();
        given(userRepository.save(any(User.class))).willReturn(createUser());

        // when
        userService.createUser(createRequest);

        // then
        then(userRepository).should().existsByEmail(anyString());
        then(userRepository).should().save(any(User.class));
    }

    @Test
    @DisplayName("이메일 중복은 회원가입에 실패한다.")
    public void emailDuplicated_createUser_failure() {
        // given
        UserDto.CreateRequest createRequest = createTestUserData();
        String existingEmail = "test@modu.com";
        given(userRepository.existsByEmail(existingEmail)).willReturn(true);

        // when
        assertThrows(DuplicatedEmailException.class, () -> userService.createUser(createRequest));

        // then
        then(userRepository).should().existsByEmail("test@modu.com");
        then(userRepository).shouldHaveNoMoreInteractions();
    }

    private UserDto.CreateRequest createTestUserData() {
        return UserDto.CreateRequest.builder()
            .email("test@modu.com")
            .password("test12345@")
            .name("테스트네임")
            .phoneNumber("01012345678")
            .build();
    }

    private User createUser() {
        return User.builder()
            .email("test@modu.com")
            .name("테스트네임")
            .password("test12345@")
            .role(UserRole.BUYER)
            .phoneNumber("01012345678")
            .build();
    }
}
