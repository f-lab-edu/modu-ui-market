package com.flab.modu.users.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.then;
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
import com.flab.modu.users.exception.DuplicatedEmailException;
import com.flab.modu.users.exception.NotExistedUserException;
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
    @DisplayName("???????????? - ??????????????? ????????????.")
    void createUser_successful() throws Exception {
        CreateRequest createRequest = getCreateRequest();

        willDoNothing().given(userService).createUser(createRequest);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andDo(document("create-user",
                requestFields(
                    fieldWithPath("email").type(JsonFieldType.STRING)
                        .description("????????? ??? ????????? ?????????"),
                    fieldWithPath("password").type(JsonFieldType.STRING)
                        .description("????????????"),
                    fieldWithPath("name").type(JsonFieldType.STRING)
                        .description("??????"),
                    fieldWithPath("phoneNumber").type(JsonFieldType.STRING)
                        .description("????????? ??????")
                )
            ));

        then(userService).should().createUser(refEq(createRequest));
    }

    @Test
    @DisplayName("???????????? - ????????? ?????? ?????? ????????? ???????????? ??????????????? ????????????.")
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
                        .description("???????????? ?????? ?????????"),
                    fieldWithPath("password").type(JsonFieldType.STRING)
                        .description("8??? ?????? 20??? ????????? ????????????"),
                    fieldWithPath("name").type(JsonFieldType.STRING)
                        .description("2??? ?????? 10??? ????????? ??????"),
                    fieldWithPath("phoneNumber").type(JsonFieldType.STRING)
                        .description("'-'??? ????????? ????????? ??????")
                )
            ));
    }

    @Test
    @DisplayName("????????? - ???????????? ????????????.")
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
                        .description("????????? ?????????"),
                    fieldWithPath("password").type(JsonFieldType.STRING)
                        .description("????????? ????????????")
                )
            ));

        then(loginService).should().login(refEq(loginRequest));
    }

    @Test
    @DisplayName("????????? - ?????????, ??????????????? ?????? ?????? ???????????? ????????????.")
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
                        .description("????????? ?????????"),
                    fieldWithPath("password").type(JsonFieldType.STRING)
                        .description("????????? ????????????")
                )
            ));
    }

    @Test
    @DisplayName("???????????? - ??????????????? ????????????.")
    public void logout_success() throws Exception {
        willDoNothing().given(loginService).logout();

        mockMvc.perform(delete("/users/logout"))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("users/login/failure"));
    }

    private CreateRequest getCreateRequest() {
        CreateRequest createRequest = CreateRequest.builder()
            .email("test123@modu.com")
            .password("test12344")
            .name("???????????????")
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
}
