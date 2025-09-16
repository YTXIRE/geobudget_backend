package com.geobudget.geobudget.validator;


import com.geobudget.geobudget.entity.User;
import com.geobudget.geobudget.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.control.MappingControl;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthValidator {
    private final UserRepository userRepository;

    public void checkRepeatPassword(String password, String repeatPassword) {
        if (!password.equals(repeatPassword)) {
            log.info("Пароли не совпадают");
            throw new IllegalArgumentException("Пароли не совпадают");
        }
    }

    public void checkLoginUnique(String login) {
        if (!userRepository.findByUsername(login).isEmpty()) {
            log.info("Пользователь с логином " + login + " уже зарегистрирован");
            throw new IllegalArgumentException("Пользователь с логином " + login + " уже зарегистрирован");
        }
    }

    public void checkEmailUnique(String email) {
        if (!userRepository.findByEmail(email).isEmpty()) {
            log.info("Пользователь с email " + email + " уже зарегистрирован");
            throw new IllegalArgumentException("Пользователь с email " + email + " уже зарегистрирован");
        }
    }

    public void checkPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            log.info("Телефон не может быть пустым");
            throw new IllegalArgumentException("Телефон не может быть пустым");
        }

        if (userRepository.findByPhone(phone).isPresent()) {
            log.info("Пользователь с телефоном " + phone + " уже зарегистрирован");
            throw new IllegalArgumentException("Пользователь с телефоном " + phone + " уже зарегистрирован");
        }
    }

    public void checkLogin(String login) {
        if (login == null || login.isEmpty()) {
            log.info("Логин не может быть пустым");
            throw new IllegalArgumentException("Логин не может быть пустым");
        }

        if (userRepository.findByUsername(login).isEmpty()) {
            log.info("Пользователь с логином " + login + " не найден");
            throw new IllegalArgumentException("Пользователь с логином " + login + " не найден");
        }
    }

    public void checkPassword(String password) {
        if (password == null || password.isEmpty()) {
            log.info("Пароль не может быть пустым");
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }
    }

    public void checkIsActive(String login) {
        User user = userRepository.findByUsername(login).get();
        if (!user.isActive()) {
            log.info("Пользователь не активирован");
            throw new IllegalArgumentException("Пользователь не активирован. Проверьте email: " + user.getEmail());
        }
    }

    public void checkExistingEmail(String email) {
        User user = userRepository.findByEmail(email).get();
        if (!user.isActive()) {
            log.info("Пользователь не найден");
            throw new IllegalArgumentException("Пользователь с таким email не найден");
        }
    }
}