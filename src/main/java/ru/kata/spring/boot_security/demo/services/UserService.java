package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    void save(User user);

    List<User> findAll();

    User findOne(int id);

    void update(int id, User updateUser);

    void delete(int id);

    UserDetails loadUserByUsername(String username);

    Optional<User> getUserToEmail (String email);

    Optional<User> findUserOptional(String username);



}
