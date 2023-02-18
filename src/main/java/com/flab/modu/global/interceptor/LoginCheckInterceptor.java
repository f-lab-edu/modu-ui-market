package com.flab.modu.global.interceptor;

import com.flab.modu.global.annotation.LoginCheck;
import com.flab.modu.users.exception.UnauthenticatedUserException;
import com.flab.modu.users.service.LoginService;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final Environment environment;
    private final LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {

        if (Arrays.stream(environment.getActiveProfiles()).anyMatch(e -> e.equals("test"))) {
            return true;
        }

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        LoginCheck loginCheck = handlerMethod.getMethodAnnotation(LoginCheck.class);
        if (loginCheck == null) {
            return true;
        }

        if (loginService.getLoginUser() == null) {
            throw new UnauthenticatedUserException();
        }

        return true;
    }
}
