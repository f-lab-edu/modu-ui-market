package com.flab.modu.admin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.flab.modu.users.controller.UserDto.UserResponse;
import com.flab.modu.users.domain.common.UserRole;
import com.flab.modu.users.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DisplayName("Admin Service 테스트")
@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminService adminService;

    @Test
    @DisplayName("정상적으로 회원조회에 성공한다.")
    public void searchUsers_successful() throws Exception {
        // given
        List<UserResponse> userResponseList = getUserResponseList();
        long size = userResponseList.size();
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserResponse> findUsers = new PageImpl<>(userResponseList, pageable, size);
        given(userRepository.searchByUsers(any(), any())).willReturn(findUsers);

        // when
        Page<UserResponse> result = adminService.findUsers(any(), any());

        // then
        assertThat(result.getTotalElements()).isEqualTo(size);
        assertThat(result.getTotalPages()).isEqualTo(1);
        then(userRepository).should().searchByUsers(any(), any());
    }

    private List<UserResponse> getUserResponseList() {
        return IntStream.range(0, 2)
            .mapToObj(i -> getUserResponse(i))
            .collect(Collectors.toList());
    }

    private UserResponse getUserResponse(int i) {
        return UserResponse.builder()
            .id((long) i)
            .email(getConcatData("test@modu.com", i))
            .name(getConcatData("testName", i))
            .phoneNumber(getConcatData("0100000000", i))
            .role(UserRole.BUYER)
            .build();
    }

    private String getConcatData(String data, int i) {
        return new StringBuilder(data).append(i).toString();
    }
}