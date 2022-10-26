package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UsersRepository;


import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService { //UserDetailsService по имени юзера предоставить юзера

    private  UsersRepository usersRepository;

    @Autowired
    public  void setUsersRepository(UsersRepository usersRepository){
        this.usersRepository = usersRepository;}
//==============================================================

    public User findByUsername(String username){
        return  usersRepository.findByUsername(username);
    }
//========================UserDetailsService======================================
    // По имени пользователя возвращает его полностью с базы данных

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }


        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }


        private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role>roles) {
            return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());



        }
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public List<User> findAll(){return usersRepository.findAll();}
    //==============================================================
    public User findOne(int id){
        Optional<User> foundUser = Optional.ofNullable(usersRepository.findById(id));
        return foundUser.orElse(null);}
    //==============================================================
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
