package com.flab.modu.users.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.modu.users.controller.UserDto.CreateRequest;
import com.flab.modu.users.exception.DuplicatedEmailException;
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
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 - 회원가입에 성공한다.")
    void createUser_successful() throws Exception {
        UserDto.CreateRequest createRequest = CreateRequest.builder()
            .email("test123@modu.com")
            .password("test12344")
            .name("테스트네임")
            .phoneNumber("01012345678")
            .build();

        doNothing().when(userService).createUser(createRequest);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andDo(document("create-user",
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
    }

    @Test
    @DisplayName("회원가입 - 잘못된 입력 또는 중복된 이메일로 회원가입에 실패한다.")
    void createUser_failure() throws Exception {
        CreateRequest createRequest = CreateRequest.builder()
            .email("test123@modu.com")
            .password("test123123122")
            .name("테스트네임")
            .phoneNumber("01012345678")
            .build();

        doThrow(new DuplicatedEmailException()).when(userService).createUser(any());

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
}
