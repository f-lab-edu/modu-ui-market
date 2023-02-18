package com.flab.modu.users.service;

import static com.flab.modu.users.domain.common.UserConstant.EMAIL;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.flab.modu.users.controller.UserDto;
import com.flab.modu.users.controller.UserDto.LoginRequest;
import com.flab.modu.users.domain.common.UserRole;
import com.flab.modu.users.domain.entity.User;
import com.flab.modu.users.encoder.PasswordEncoder;
import com.flab.modu.users.exception.NotExistedUserException;
import com.flab.modu.users.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginService loginService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MockHttpSession session;

    private final String USER_EMAIL = "test123@modu.com";
    private final String USER_PASSWORD = "testPwd";

    @Test
    @DisplayName("로그인 - 이메일과 패스워드를 잘못 입력해 로그인에 실패한다..")
    public void notExistUser() throws Exception {
        // given
        LoginRequest loginRequest = loginRequest();
        given(userRepository.findByEmailAndPassword(loginRequest.getEmail(),
            passwordEncoder.encrypt(loginRequest().getPassword()))).willReturn(Optional.empty());

        // when
        assertThrows(NotExistedUserException.class, () -> loginService.login(loginRequest));

        // then
        then(userRepository).should().findByEmailAndPassword(loginRequest.getEmail(),
            passwordEncoder.encrypt(loginRequest().getPassword()));
        then(session).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("로그인 - 이메일, 패스워드가 일치하는 사용자는 로그인에 성공한다.")
    public void login_successful() throws Exception {
        // given
        LoginRequest loginRequest = loginRequest();
        given(userRepository.findByEmailAndPassword(USER_EMAIL, passwordEncoder.encrypt(USER_PASSWORD))).willReturn(Optional.of(createUser()));

        // when
        loginService.login(loginRequest);

        // then
        then(userRepository).should().findByEmailAndPassword(USER_EMAIL, passwordEncoder.encrypt(USER_PASSWORD));
        then(session).should().setAttribute(EMAIL, USER_EMAIL);
    }

    private UserDto.LoginRequest loginRequest() {
        return LoginRequest.builder()
            .email(USER_EMAIL)
            .password(USER_PASSWORD)
            .build();
    }

    private User createUser() {
        return User.builder()
            .email(USER_EMAIL)
            .name("테스트네임")
            .password(USER_PASSWORD)
            .role(UserRole.BUYER)
            .phoneNumber("01012345678")
            .build();
    }
}
