package com.flab.modu.users.service;

import com.flab.modu.users.controller.UserDto.SaveRequest;
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

    public void createUser(SaveRequest saveRequest) {
        if (checkEmailDuplicate(saveRequest.getEmail())) {
            throw new DuplicatedEmailException();
        }
        userRepository.save(saveRequest.toEntity());
    }

    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }
}
