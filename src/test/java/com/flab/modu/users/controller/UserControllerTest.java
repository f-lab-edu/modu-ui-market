package com.flab.modu.users.controller;

import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.modu.users.controller.UserDto.CreateRequest;
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
            .password("test123")
            .name("정찬우")
            .phoneNumber("01020881464")
            .build();

        doNothing().when(userService).createUser(createRequest);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andDo(document("create-user", requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING)
                    .description("email address"),
                fieldWithPath("password").type(JsonFieldType.STRING)
                    .description("password"),
                fieldWithPath("name").type(JsonFieldType.STRING)
                    .description("name"),
                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("phoneNumber")
            )));
    }
}