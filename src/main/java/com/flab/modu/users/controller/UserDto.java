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
    public static class SaveRequest {
        @NotNull
        private String email;

        @NotNull
        private String password;

        @NotNull
        private String name;
        private UserRole role;
        private String phoneNumber;

        @Builder
        public SaveRequest(String email, String password, String name, UserRole role,
            String phoneNumber) {
            this.email = email;
            this.password = password;
            this.name = name;
            this.role = role;
            this.phoneNumber = phoneNumber;
        }

        public User toEntity() {
            return User.builder()
                .email(email)
                .password(password)
                .name(name)
                .role(role)
                .phoneNumber(phoneNumber)
                .build();
        }
    }
}
