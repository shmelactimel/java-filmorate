package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UserTest {
    Validator validator;
    User user;

    @BeforeEach
    public void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void testUserCreationWithoutEmail() {
        user = new User();
        user.setLogin("testLogin");
        user.setName("test Name");
        user.setBirthday(LocalDate.of(1990, Month.DECEMBER, 20));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 2);
    }

    @Test
    public void testUserCreationWithEmailBlank() {
        user = new User();
        user.setEmail("");
        user.setLogin("testLogin");
        user.setName("test Name");
        user.setBirthday(LocalDate.of(1990, Month.DECEMBER, 20));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
    }

    @Test
    public void testUserCreationWithIncorrectEmail() {
        user = new User();
        user.setEmail("test Incorrect email @");
        user.setLogin("testLogin");
        user.setName("test Name");
        user.setBirthday(LocalDate.of(1990, Month.DECEMBER, 20));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
    }

    @Test
    public void testUserCreationWithoutLogin() {
        user = new User();
        user.setEmail("user@yandex.com");
        user.setName("test Name");
        user.setBirthday(LocalDate.of(1990, Month.DECEMBER, 20));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 2);
    }

    @Test
    public void testUserCreationWithLoginBlank() {
        user = new User();
        user.setEmail("user@yandex.com");
        user.setLogin("");
        user.setName("test Name");
        user.setBirthday(LocalDate.of(1990, Month.DECEMBER, 20));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 2);
    }

    @Test
    public void testUserCreationWithSpacesInLogin() {
        user = new User();
        user.setEmail("user@yandex.com");
        user.setLogin("Some Spaces In Test Login");
        user.setName("test Name");
        user.setBirthday(LocalDate.of(1990, Month.DECEMBER, 20));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
    }

    @Test
    public void testUserCreationWithoutBirthday() {
        user = new User();
        user.setEmail("user@yandex.com");
        user.setLogin("SomeTestLogin");
        user.setName("test Name");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
    }

    @Test
    public void testUserCreationWithBirthdayInFuture() {
        user = new User();
        user.setEmail("user@yandex.com");
        user.setLogin("SomeTestLogin");
        user.setName("test Name");
        user.setBirthday(LocalDate.of(2077, Month.APRIL, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
    }
}
