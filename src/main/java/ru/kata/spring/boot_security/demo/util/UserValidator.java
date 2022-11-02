package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;

@Component
public class UserValidator  implements Validator {


    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserValidator(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public boolean supports(Class<?> clazz) {

        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        User user = (User) target;

        if (userServiceImpl.getUserToEmail(user.getEmail()).isPresent())

            errors.rejectValue("email", "", "This email is already taken");

        if (userServiceImpl.findUserOptional(user.getUsername()).isPresent())

            errors.rejectValue("username", "", "This Username is already taken");


    }

}
