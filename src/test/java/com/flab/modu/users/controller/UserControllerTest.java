package com.flab.modu.users.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.modu.users.controller.UserDto.CreateRequest;
import com.flab.modu.users.controller.UserDto.LoginRequest;
import com.flab.modu.users.controller.UserDto.PasswordRequest;
import com.flab.modu.users.exception.DuplicatedEmailException;
import com.flab.modu.users.exception.NotExistedUserException;
import com.flab.modu.users.exception.WrongPasswordException;
import com.flab.modu.users.service.LoginService;
import com.flab.modu.users.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
@WebMvcTest(UserController.class)
@ActiveProfiles("test")
class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private LoginService loginService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 - 회원가입에 성공한다.")
    void createUser_successful() throws Exception {
        CreateRequest createRequest = getCreateRequest();

        willDoNothing().given(userService).createUser(createRequest);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andDo(document("users/create/success",
                requestFields(
                    fieldWithPath("email").type(JsonFieldType.STRING)
                        .description("로그인 시 사용할 이메일"),
                    fieldWithPath("password").type(JsonFieldType.STRING)
                        .description("비밀번호"),
                    fieldWithPath("name").type(JsonFieldType.STRING)
                        .description("이름"),
                    fieldWithPath("phoneNumber").type(JsonFieldType.STRING)
                        .description("휴대폰 번호")
                )
            ));

        then(userService).should().createUser(refEq(createRequest));
    }

    @Test
    @DisplayName("회원가입 - 잘못된 입력 또는 중복된 이메일로 회원가입에 실패한다.")
    void createUser_failure() throws Exception {
        CreateRequest createRequest = getCreateRequest();

        willThrow(new DuplicatedEmailException()).given(userService).createUser(any());

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
            .andDo(print())
            .andExpect(status().isConflict())
            .andDo(document("users/create/failure",
                requestFields(
                    fieldWithPath("email").type(JsonFieldType.STRING)
                        .description("중복되지 않은 이메일"),
                    fieldWithPath("password").type(JsonFieldType.STRING)
                        .description("8자 이상 20자 이하의 비밀번호"),
                    fieldWithPath("name").type(JsonFieldType.STRING)
                        .description("2자 이상 10자 이하의 이름"),
                    fieldWithPath("phoneNumber").type(JsonFieldType.STRING)
                        .description("'-'을 제외한 휴대폰 번호")
                )
            ));
    }

    @Test
    @DisplayName("로그인 - 로그인에 성공한다.")
    void login_success() throws Exception {
        LoginRequest loginRequest = getLoginRequest();

        willDoNothing().given(loginService).login(loginRequest);

        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("users/login/success",
                requestFields(
                    fieldWithPath("email").type(JsonFieldType.STRING)
                        .description("사용자 이메일"),
                    fieldWithPath("password").type(JsonFieldType.STRING)
                        .description("사용자 패스워드")
                )
            ));

        then(loginService).should().login(refEq(loginRequest));
    }

    @Test
    @DisplayName("로그인 - 이메일, 패스워드가 맞지 않아 로그인에 실패한다.")
    void login_failure() throws Exception {
        LoginRequest loginRequest = getLoginRequest();

        willThrow(new NotExistedUserException()).given(loginService).login(any());

        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andDo(document("users/login/failure",
                requestFields(
                    fieldWithPath("email").type(JsonFieldType.STRING)
                        .description("사용자 이메일"),
                    fieldWithPath("password").type(JsonFieldType.STRING)
                        .description("사용자 패스워드")
                )
            ));
    }

    @Test
    @DisplayName("로그아웃 - 로그아웃에 성공한다.")
    public void logout_success() throws Exception {
        willDoNothing().given(loginService).logout();

        mockMvc.perform(delete("/users/logout"))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("users/login/failure"));
    }

    @Test
    @DisplayName("회원탈퇴 - 회원탈퇴에 성공한다.")
    void deleteUser_successful() throws Exception {
        PasswordRequest passwordRequest = getPasswordRequest();
        String password = passwordRequest.getPassword();

        String email = "test@modu.com";
        willAnswer(invocation -> email).given(loginService).getLoginUser();
        willDoNothing().given(userService).delete(email, password);
        willDoNothing().given(loginService).logout();

        mockMvc.perform(delete("/users")
                .header("email", email)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordRequest)))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("users/delete/success",
                requestFields(
                    fieldWithPath("password").type(JsonFieldType.STRING)
                        .description("비밀번호")
                )
            ));

        then(loginService).should().getLoginUser();
        then(userService).should().delete(email, password);
        then(loginService).should().logout();
    }

    @Test
    @DisplayName("회원탈퇴 - 요청정보가 일치하지 않아 회원탈퇴에 실패한다.")
    void deleteUser_failure() throws Exception {
        PasswordRequest passwordRequest = getPasswordRequest();
        String password = passwordRequest.getPassword();

        String email = "test@modu.com";
        willAnswer(invocation -> email).given(loginService).getLoginUser();
        willThrow(new WrongPasswordException()).given(userService).delete(email, password);

        mockMvc.perform(delete("/users")
                .header("email", email)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordRequest)))
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andDo(document("users/delete/failure",
                requestFields(
                    fieldWithPath("password").type(JsonFieldType.STRING)
                        .description("비밀번호")
                )
            ));

        then(loginService).should().getLoginUser();
    }

    private CreateRequest getCreateRequest() {
        CreateRequest createRequest = CreateRequest.builder()
            .email("test123@modu.com")
            .password("test12344")
            .name("테스트네임")
            .phoneNumber("01012345678")
            .build();
        return createRequest;
    }

    private LoginRequest getLoginRequest() {
        return LoginRequest.builder()
            .email("test123@modu.com")
            .password("test1234455")
            .build();
    }

    private PasswordRequest getPasswordRequest() {
        return PasswordRequest.builder()
            .password("test12344")
            .build();
    }
}
