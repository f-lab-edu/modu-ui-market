package com.flab.modu.users.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.flab.modu.users.controller.UserDto;
import com.flab.modu.users.domain.common.UserRole;
import com.flab.modu.users.domain.entity.User;
import com.flab.modu.users.encoder.PasswordEncoder;
import com.flab.modu.users.exception.DuplicatedEmailException;
import com.flab.modu.users.exception.NotExistedUserException;
import com.flab.modu.users.exception.WrongPasswordException;
import com.flab.modu.users.repository.UserRepository;
import java.util.Optional;
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

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private final String EXISTING_EMAIL = "test@modu.com";

    private final String NOT_EXISTING_EMAIL = "test123@modu.com";

    private final String CORRECT_PASSWORD = "test12345@";

    private final String WRONG_PASSWORD = "test12345!";

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
        given(userRepository.existsByEmail(EXISTING_EMAIL)).willReturn(true);

        // when
        assertThrows(DuplicatedEmailException.class, () -> userService.createUser(createRequest));

        // then
        then(userRepository).should().existsByEmail(EXISTING_EMAIL);
        then(userRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("회원탈퇴에 성공한다.")
    public void deleteUser_successful() throws Exception {
        // given
        User user = createUser();
        given(userRepository.existsByEmail(EXISTING_EMAIL)).willReturn(true);
        given(userRepository.findByEmailAndPassword(EXISTING_EMAIL,
            passwordEncoder.encrypt(CORRECT_PASSWORD))).willReturn(Optional.of(user));

        // when
        userService.delete(EXISTING_EMAIL, CORRECT_PASSWORD);

        // then
        then(userRepository).should().existsByEmail(EXISTING_EMAIL);
        then(userRepository).should()
            .findByEmailAndPassword(EXISTING_EMAIL, passwordEncoder.encrypt(CORRECT_PASSWORD));
        then(userRepository).should().deleteByEmail(EXISTING_EMAIL);
    }

    @Test
    @DisplayName("이메일이 존재하지 않는 경우 회원탈퇴에 실패한다.")
    public void notExistEmail_deleteUser_failure() throws Exception {
        // given
        given(userRepository.existsByEmail(NOT_EXISTING_EMAIL)).willThrow(new NotExistedUserException());

        // when
        assertThrows(NotExistedUserException.class, () -> userService.delete(NOT_EXISTING_EMAIL, CORRECT_PASSWORD));

        // then
        then(userRepository).should().existsByEmail(NOT_EXISTING_EMAIL);
        then(userRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("비밀번호를 틀린 경우 회원탈퇴에 실패한다.")
    public void wrongPassword_deleteUser_failure() throws Exception {
        // given
        User user = createUser();
        given(userRepository.existsByEmail(EXISTING_EMAIL)).willReturn(true);
        given(userRepository.findByEmailAndPassword(EXISTING_EMAIL,
            passwordEncoder.encrypt(WRONG_PASSWORD))).willThrow(
            new WrongPasswordException());

        // when
        assertThrows(WrongPasswordException.class, () -> userService.delete(EXISTING_EMAIL, WRONG_PASSWORD));

        // then
        then(userRepository).should().existsByEmail(EXISTING_EMAIL);
        then(userRepository).should()
            .findByEmailAndPassword(EXISTING_EMAIL, passwordEncoder.encrypt(WRONG_PASSWORD));
        then(userRepository).shouldHaveNoMoreInteractions();
    }

    private UserDto.CreateRequest createTestUserData() {
        return UserDto.CreateRequest.builder()
            .email(EXISTING_EMAIL)
            .password(CORRECT_PASSWORD)
            .name("테스트네임")
            .phoneNumber("01012345678")
            .build();
    }

    private User createUser() {
        return User.builder()
            .email(EXISTING_EMAIL)
            .name("테스트네임")
            .password(CORRECT_PASSWORD)
            .role(UserRole.BUYER)
            .phoneNumber("01012345678")
            .build();
    }
}
