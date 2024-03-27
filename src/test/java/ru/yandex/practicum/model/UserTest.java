package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.time.LocalDate;

import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistException;

public class UserTest {
    private Validator validator;
    private User user;

    @BeforeEach
    void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        String email = "user@user.com";
        String login = "userLogin";
        String name = "userName";
        LocalDate date = LocalDate.of(2000,1,1);
        user = new User();
        user.setEmail(email);
        user.setLogin(login);
        user.setName(name);
        user.setBirthday(date);
    }

    @Test
    void validateUserEmail_failure_emptyEmail() throws EntityAlreadyExistException {
        //given
        String email = "";
        user.setEmail(email);
        //when
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        //then
        assertFalse(violations.isEmpty());
    }

    @Test
    void validateUserEmail_failure_nullEmail() throws EntityAlreadyExistException {
        //given
        String email = null;
        user.setEmail(email);
        //when
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        //then
        assertFalse(violations.isEmpty());
    }

    @Test
    void validateUserLogin_failure_emptyLogin() throws EntityAlreadyExistException {
        //given
        String login = "";
        user.setLogin(login);
        //when
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        //then
        assertFalse(violations.isEmpty());
    }

    @Test
    void validateUserLogin_failure_loginWithSpecialCharacter() throws EntityAlreadyExistException {
        //given
        String login = "log in";
        user.setLogin(login);
        //when
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        //then
        assertFalse(violations.isEmpty());
    }

    @Test
    void validateUserLogin_failure_nullLogin() throws EntityAlreadyExistException {
        //given
        String login = null;
        user.setLogin(login);
        //when
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        //then
        assertFalse(violations.isEmpty());
    }

    @Test
    void validateUserBirthday_failure_futureBirthday() throws EntityAlreadyExistException {
        //given
        LocalDate date = LocalDate.of(4000,1,1);
        user.setBirthday(date);
        //when
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        //then
        assertFalse(violations.isEmpty());
    }

    @Test
    void validateUserBirthday_failure_nullBirthday() throws EntityAlreadyExistException {
        //given
        LocalDate date = null;
        user.setBirthday(date);
        //when
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        //then
        assertFalse(violations.isEmpty());
    }
}