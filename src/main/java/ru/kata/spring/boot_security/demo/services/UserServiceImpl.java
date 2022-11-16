package ru.kata.spring.boot_security.demo.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UsersRepository;
import ru.kata.spring.boot_security.demo.util.UserNotFoundException;


import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UsersRepository usersRepository, ModelMapper modelMapper) {

        this.usersRepository = usersRepository;
        this.modelMapper = modelMapper;
    }


//  loadUserByUsername
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
        Optional<User> foundUser = Optional.ofNullable(usersRepository.findById(id));

        return foundUser.orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<User> findAll() {return usersRepository.findAll();}

    @Override
    public void save(User user) {
        usersRepository.save(user);
    }

    @Override
    public void update(int id, User updateUser){updateUser.setId(id);usersRepository.update(updateUser);}

    @Override
    public void delete(int id) {usersRepository.deleteById(id);}

//ConvertTo
    public User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

//Optional
    @Override
    public Optional <User> getUserToEmail (String email) {return  usersRepository.getUserByEmail(email);}

    @Override
    public Optional<User> findUserOptional(String username) {return usersRepository.findUserOptional(username);}

   }
