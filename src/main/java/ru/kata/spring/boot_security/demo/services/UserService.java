package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UsersRepository;


import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService { //UserDetailsService по имени юзера предоставить всего юзера

    private final RoleRepository roleRepository;
    private UsersRepository usersRepository;
    @Autowired
    public UserService(UsersRepository usersRepository, RoleRepository roleRepository) {
        this.usersRepository = usersRepository;
        this.roleRepository = roleRepository;
    }

//====================ALL<USER> ALL<ROLE>=======================

    public List<User> findAll(){return usersRepository.findAll();}
    public List<Role> getAllRoles() { return roleRepository.findAll(); }

//==============================================================
    public User findByUsername(String username){

        return  usersRepository.findByUsername(username);
    }

//========================UserDetailsService======================================
    // По имени пользователя возвращает его полностью с базы данных

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {

        User user = usersRepository.findByUsername(username);
        return user;

    }

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role>roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }

//=========================findUserById=====================================
    public User findOne(int id){
        Optional<User> foundUser = Optional.ofNullable(usersRepository.findById(id));
        return foundUser.orElse(null);}

//==========================saveUser====================================
    @Transactional
    public void save(User user){usersRepository.save(user);}
//==============================================================
    @Transactional
    public void update(int id, User updateUser){
        updateUser.setId(id);
        usersRepository.update(updateUser);
    }
//==============================================================
    @Transactional
    public void delete(int id){
        usersRepository.deleteById(id);
    }

//==============================================================
}
