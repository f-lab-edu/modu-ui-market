package com.flab.modu.users.service;

import static com.flab.modu.users.domain.common.UserConstant.EMAIL;

import com.flab.modu.users.controller.UserDto;
import com.flab.modu.users.domain.entity.User;
import com.flab.modu.users.encoder.PasswordEncoder;
import com.flab.modu.users.exception.NotExistedUserException;
import com.flab.modu.users.repository.UserRepository;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginService {

    private final HttpSession session;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public void login(UserDto.LoginRequest loginRequest) {
        loginRequest.encryptPassword(passwordEncoder);
        User findUser = userRepository.findByEmailAndPassword(loginRequest.getEmail(),
                loginRequest.getPassword())
            .orElseThrow(NotExistedUserException::new);

        session.setAttribute(EMAIL, findUser.getEmail());
    }

    public void logout() {
        session.invalidate();
    }

    public String getLoginUser() {
        return (String) session.getAttribute(EMAIL);
    }
}
