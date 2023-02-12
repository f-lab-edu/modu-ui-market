package com.flab.modu.users.service;

import com.flab.modu.users.controller.UserDto;
import com.flab.modu.users.exception.DuplicatedEmailException;
import com.flab.modu.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public void createUser(UserDto.CreateRequest createRequest) {
        if (checkEmailDuplicate(createRequest.getEmail())) {
            throw new DuplicatedEmailException();
        }
        userRepository.save(createRequest.toEntity());
    }

    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }
}
