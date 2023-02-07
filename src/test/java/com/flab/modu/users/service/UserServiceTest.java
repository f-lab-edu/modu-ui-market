package com.flab.modu.users.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.flab.modu.users.controller.UserDto;
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

    private UserDto.CreateRequest createTestUserData() {
        return UserDto.CreateRequest.builder()
            .email("test@modu.com")
            .password("test123")
            .name("test")
            .build();
    }

    @Test
    @DisplayName("이메일 중복은 회원가입에 실패한다.")
    public void emailDuplicate() {
        // given
        UserDto.CreateRequest createRequest = createTestUserData();
        given(userRepository.existsByEmail("test@modu.com")).willReturn(true);

        // when
        assertThrows(DuplicatedEmailException.class, () -> userService.createUser(createRequest));

        // then
        then(userRepository).should().existsByEmail("test@modu.com");
    }
}