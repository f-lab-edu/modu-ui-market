package com.flab.modu.users.service;

import com.flab.modu.users.controller.UserDto;
import com.flab.modu.users.encoder.PasswordEncoder;
import com.flab.modu.users.exception.DuplicatedEmailException;
import com.flab.modu.users.exception.NotExistedUserException;
import com.flab.modu.users.exception.WrongPasswordException;
import com.flab.modu.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public void createUser(UserDto.CreateRequest createRequest) {
        if (checkEmailDuplicate(createRequest.getEmail())) {
            throw new DuplicatedEmailException();
        }
        createRequest.encryptPassword(passwordEncoder);

        userRepository.save(createRequest.toEntity());
    }

    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public void delete(String email, String password) {
        if (!checkEmailDuplicate(email)) {
            new NotExistedUserException();
        }

        userRepository.findByEmailAndPassword(email, passwordEncoder.encrypt(password))
            .orElseThrow(() -> new WrongPasswordException());

        userRepository.deleteByEmail(email);
    }
}
