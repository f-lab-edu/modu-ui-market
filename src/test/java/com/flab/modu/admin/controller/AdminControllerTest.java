package com.flab.modu.admin.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.modu.admin.service.AdminService;
import com.flab.modu.users.controller.UserDto.UserResponse;
import com.flab.modu.users.domain.common.UserRole;
import com.flab.modu.users.service.LoginService;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @MockBean
    private AdminService adminService;

    @MockBean
    private LoginService loginService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static List<UserResponse> userResponseList;

    private static final int USERS_SIZE = 10;

    @BeforeAll
    static void init() {
        userResponseList = IntStream.range(0, USERS_SIZE)
            .mapToObj(i -> getUserResponse(i))
            .collect(Collectors.toList());
    }

    private static UserResponse getUserResponse(int i) {
        return UserResponse.builder()
            .id((long) i)
            .email(getConcatData("test@modu.com", i))
            .name(getConcatData("testName", i))
            .phoneNumber(getConcatData("0100000000", i))
            .role(UserRole.BUYER)
            .build();
    }

    private static String getConcatData(String data, int i) {
        return new StringBuilder(data).append(i).toString();
    }

    @Test
    @DisplayName("회원조회 - 회원 전체 조회에 성공한다.")
    public void findAllUsers_successful() throws Exception {
        // given
        Pageable pageable = getPageable();
        PageImpl<UserResponse> result = new PageImpl<>(userResponseList, pageable,
            USERS_SIZE);
        given(adminService.findUsers(any(), any())).willReturn(result);

        MultiValueMap<String, String> queryString = new LinkedMultiValueMap<>();
        queryString.add("page", "0");
        queryString.add("size", "10");

        // when
        mockMvc.perform(get("/admin/users")
                .params(queryString))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.[0].id").value(userResponseList.get(0).getId()))
            .andExpect(jsonPath("$.content.[0].email").value(userResponseList.get(0).getEmail()))
            .andExpect(jsonPath("$.content.[0].name").value(userResponseList.get(0).getName()))
            .andExpect(
                jsonPath("$.content.[0].role").value(userResponseList.get(0).getRole().toString()))
            .andExpect(jsonPath("$.content.[0].phoneNumber").value(
                userResponseList.get(0).getPhoneNumber()))
            .andExpect(jsonPath("$.content.[1].id").value(userResponseList.get(1).getId()))
            .andExpect(jsonPath("$.content.[1].email").value(userResponseList.get(1).getEmail()))
            .andExpect(jsonPath("$.content.[1].name").value(userResponseList.get(1).getName()))
            .andExpect(
                jsonPath("$.content.[1].role").value(userResponseList.get(1).getRole().toString()))
            .andExpect(jsonPath("$.content.[1].phoneNumber").value(
                userResponseList.get(1).getPhoneNumber()))
            .andExpect(jsonPath("$.totalElements").value(USERS_SIZE))
            .andDo(print())
            .andDo(document("admin/users/findAll",
                requestParameters(
                    parameterWithName("page").description("페이지 번호"),
                    parameterWithName("size").description("한 페이지 사이즈")
                ),
                responseFields(
                    fieldWithPath("content.[].id").type(JsonFieldType.NUMBER).description("아이디"),
                    fieldWithPath("content.[].email").type(JsonFieldType.STRING)
                        .description("이메일"),
                    fieldWithPath("content.[].name").type(JsonFieldType.STRING)
                        .description("회원명"),
                    fieldWithPath("content.[].role").type(JsonFieldType.STRING)
                        .description("역할"),
                    fieldWithPath("content.[].phoneNumber").type(JsonFieldType.STRING)
                        .description("전화번호"),
                    fieldWithPath("pageable.offset").ignored(),
                    fieldWithPath("pageable.pageSize").ignored(),
                    fieldWithPath("pageable.pageNumber").ignored(),
                    fieldWithPath("pageable.paged").ignored(),
                    fieldWithPath("pageable.unpaged").ignored(),
                    fieldWithPath("pageable.sort.sorted").ignored(),
                    fieldWithPath("pageable.sort.unsorted").ignored(),
                    fieldWithPath("pageable.sort.empty").ignored(),
                    fieldWithPath("sort.empty").ignored(),
                    fieldWithPath("sort.sorted").ignored(),
                    fieldWithPath("sort.unsorted").ignored(),
                    fieldWithPath("totalPages").ignored(),
                    fieldWithPath("size").ignored(),
                    fieldWithPath("number").ignored(),
                    fieldWithPath("first").ignored(),
                    fieldWithPath("last").ignored(),
                    fieldWithPath("numberOfElements").ignored(),
                    fieldWithPath("empty").ignored(),
                    fieldWithPath("totalElements").ignored()
                )
            ));

        // then
        then(adminService).should().findUsers(any(), any());
    }

    @Test
    @DisplayName("회원조회[이름] - 특정 회원 조회에 성공한다.")
    public void findUserByName_successful() throws Exception {
        // given
        Pageable pageable = getPageable();
        int findIndex = 2;
        String name = userResponseList.get(findIndex).getName();
        List<UserResponse> list = getUserListByName(name);
        PageImpl<UserResponse> result = new PageImpl<>(list, pageable, USERS_SIZE);
        given(adminService.findUsers(any(), any())).willReturn(result);

        MultiValueMap<String, String> queryString = new LinkedMultiValueMap<>();
        queryString.add("page", "0");
        queryString.add("size", "10");
        queryString.add("name", name);

        // when
        mockMvc.perform(get("/admin/users")
                .params(queryString))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.[0].id").value(userResponseList.get(findIndex).getId()))
            .andExpect(jsonPath("$.content.[0].email").value(userResponseList.get(findIndex).getEmail()))
            .andExpect(jsonPath("$.content.[0].name").value(userResponseList.get(findIndex).getName()))
            .andExpect(
                jsonPath("$.content.[0].role").value(userResponseList.get(findIndex).getRole().toString()))
            .andExpect(jsonPath("$.content.[0].phoneNumber").value(
                userResponseList.get(findIndex).getPhoneNumber()))
            .andDo(print())
            .andDo(document("admin/users/findByName",
                requestParameters(
                    parameterWithName("page").description("페이지 번호"),
                    parameterWithName("size").description("한 페이지 사이즈"),
                    parameterWithName("name").description("회원명")
                ),
                responseFields(
                    fieldWithPath("content.[].id").type(JsonFieldType.NUMBER).description("아이디"),
                    fieldWithPath("content.[].email").type(JsonFieldType.STRING)
                        .description("이메일"),
                    fieldWithPath("content.[].name").type(JsonFieldType.STRING)
                        .description("회원명"),
                    fieldWithPath("content.[].role").type(JsonFieldType.STRING)
                        .description("역할"),
                    fieldWithPath("content.[].phoneNumber").type(JsonFieldType.STRING)
                        .description("전화번호"),
                    fieldWithPath("pageable.offset").ignored(),
                    fieldWithPath("pageable.pageSize").ignored(),
                    fieldWithPath("pageable.pageNumber").ignored(),
                    fieldWithPath("pageable.paged").ignored(),
                    fieldWithPath("pageable.unpaged").ignored(),
                    fieldWithPath("pageable.sort.sorted").ignored(),
                    fieldWithPath("pageable.sort.unsorted").ignored(),
                    fieldWithPath("pageable.sort.empty").ignored(),
                    fieldWithPath("sort.empty").ignored(),
                    fieldWithPath("sort.sorted").ignored(),
                    fieldWithPath("sort.unsorted").ignored(),
                    fieldWithPath("totalPages").ignored(),
                    fieldWithPath("size").ignored(),
                    fieldWithPath("number").ignored(),
                    fieldWithPath("first").ignored(),
                    fieldWithPath("last").ignored(),
                    fieldWithPath("numberOfElements").ignored(),
                    fieldWithPath("empty").ignored(),
                    fieldWithPath("totalElements").ignored()
                )
            ));

        // then
        then(adminService).should().findUsers(any(), any());
    }

    private Pageable getPageable() {
        return PageRequest.of(0, 10);
    }

    private List<UserResponse> getUserListByName(String name) {
        return userResponseList.stream().filter(o -> o.getName().equals(name))
            .collect(Collectors.toList());
    }
}
