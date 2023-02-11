package com.flab.modu.users.controller;

import com.flab.modu.users.domain.common.UserRole;
import com.flab.modu.users.domain.entity.User;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateRequest {
        @NotNull
        private String email;

        @NotNull
        private String password;

        @NotNull
        private String name;
        private String phoneNumber;

        @Builder
        public CreateRequest(String email, String password, String name, String phoneNumber) {
            this.email = email;
            this.password = password;
            this.name = name;
            this.phoneNumber = phoneNumber;
        }

        public User toEntity() {
            return User.builder()
                .email(email)
                .password(password)
                .name(name)
                .role(UserRole.BUYER)
                .phoneNumber(phoneNumber)
                .build();
        }
    }
}