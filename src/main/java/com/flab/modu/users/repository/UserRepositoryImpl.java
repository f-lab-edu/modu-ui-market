package com.flab.modu.users.repository;

import static com.flab.modu.users.domain.entity.QUser.user;
import static org.springframework.util.StringUtils.hasText;

import com.flab.modu.users.controller.UserDto.UserResponse;
import com.flab.modu.users.controller.UserDto.UserSearchCondition;
import com.flab.modu.users.domain.common.UserRole;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<UserResponse> searchByUsers(UserSearchCondition searchRequest, Pageable pageable) {

        List<UserResponse> users = getUsers(searchRequest, pageable);

        Long count = getCount(searchRequest);

        return new PageImpl<>(users, pageable, count);
    }

    private List<UserResponse> getUsers(UserSearchCondition searchRequest, Pageable pageable) {
        return jpaQueryFactory
            .select(Projections.fields(UserResponse.class,
                user.id,
                user.email,
                user.name,
                user.role,
                user.role,
                user.phoneNumber))
            .from(user)
            .where(
                userIdEq(searchRequest.getId()),
                userEmailEq(searchRequest.getEmail()),
                userNameEq(searchRequest.getName()),
                userRoleEq(searchRequest.getRole()),
                userPhoneEq(searchRequest.getPhoneNumber())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    private Long getCount(UserSearchCondition searchRequest) {
        return jpaQueryFactory.select(user.count())
            .from(user)
            .where(
                userIdEq(searchRequest.getId()),
                userEmailEq(searchRequest.getEmail()),
                userNameEq(searchRequest.getName()),
                userRoleEq(searchRequest.getRole()),
                userPhoneEq(searchRequest.getPhoneNumber())
            )
            .fetchOne();
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId != null ? user.id.eq(userId) : null;
    }

    private BooleanExpression userEmailEq(String userEmail) {
        return hasText(userEmail) ? user.email.contains(userEmail) : null;
    }

    private BooleanExpression userNameEq(String userName) {
        return hasText(userName) ? user.name.contains(userName) : null;
    }

    private BooleanExpression userRoleEq(UserRole userRole) {
        return userRole != null ? user.role.eq(userRole) : null;
    }

    private BooleanExpression userPhoneEq(String userPhoneNumber) {
        return hasText(userPhoneNumber) ? user.phoneNumber.contains(userPhoneNumber) : null;
    }
}
