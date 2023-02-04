package com.flab.modu.users.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.flab.modu.users.controller.UserDto.SaveRequest;
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

    private SaveRequest createTestUserData() {
        return SaveRequest.builder()
            .email("test@modu.com")
            .password("test123")
            .name("test")
            .build();
    }

    @Test
    @DisplayName("이메일 중복은 회원가입에 실패한다.")
    public void emailDuplicate() {
        // given
        SaveRequest testUserData = createTestUserData();
        userService.createUser(testUserData);
        String existingEmail = "test@modu.com";

        // when
        when(userRepository.existsByEmail(existingEmail)).thenReturn(true);
        SaveRequest saveRequest = SaveRequest.builder()
            .email(existingEmail)
            .password("test")
            .name("test")
            .build();

        // then
        assertThrows(DuplicatedEmailException.class, () -> userService.createUser(saveRequest));
        verify(userRepository, atLeastOnce()).existsByEmail(existingEmail);
    }
}