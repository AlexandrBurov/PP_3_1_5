package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UsersRepository;


import java.util.*;

@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UsersRepository usersRepository;
    @Autowired
    public UserService(UsersRepository usersRepository) {

        this.usersRepository = usersRepository;}

//==============================================================
//                    loadUserByUsername

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = usersRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return user;
    }

//=========================findUserById===========================
    public User findOne(int id){

        return usersRepository.findById(id);
    }
//================================================================
    public List<User> findAll() {

        return usersRepository.findAll();
    }

//==========================saveUser=============================
    @Transactional
    public boolean save(User user) {

        User userFromDb = usersRepository.findByUsername(user.getUsername());
        if (userFromDb != null) {return false;}

        usersRepository.save(user);
        return  true;
    }
//===============================================================
    @Transactional
    public void update(int id, User updateUser){

        updateUser.setId(id);

        usersRepository.update(updateUser);
    }
//===============================================================
    @Transactional
    public void delete(int id) {

        usersRepository.deleteById(id);
    }
//===============================================================
public Optional <User> getUserToEmail (String email) {

      return  usersRepository.getUserByEmail(email);
}
//===============================================================
public Optional<User> findUserOptional(String username) {

     return usersRepository.findUserOptional(username);
    }
//===============================================================
}
