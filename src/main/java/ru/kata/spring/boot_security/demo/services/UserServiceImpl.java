package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UsersRepository;


import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;

    @Autowired
    public UserServiceImpl(UsersRepository usersRepository) {

        this.usersRepository = usersRepository;}


//                    loadUserByUsername

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = usersRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return user;

    }


    @Override
    public User findOne(int id){

        return usersRepository.findById(id);
    }

    @Override
    public List<User> findAll() {

        return usersRepository.findAll();
    }


    @Override
    public void save(User user) {

        usersRepository.save(user);
    }

    @Override
    public void update(int id, User updateUser){

        updateUser.setId(id);

        usersRepository.update(updateUser);
    }


    @Override
    public void delete(int id) {

        usersRepository.deleteById(id);
    }

    @Override
    public Optional <User> getUserToEmail (String email) {

          return  usersRepository.getUserByEmail(email);
    }

    @Override
    public Optional<User> findUserOptional(String username) {

         return usersRepository.findUserOptional(username);
    }

   }
