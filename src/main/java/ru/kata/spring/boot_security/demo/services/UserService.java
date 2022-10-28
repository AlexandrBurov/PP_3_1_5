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
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UsersRepository;


import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private UsersRepository usersRepository;
    @Autowired
    public UserService(UsersRepository usersRepository) {

        this.usersRepository = usersRepository;
    }

//==============================================================
//                    loadUserByUsername

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = usersRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

//==============================================================

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role>roles) {

        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

//=========================findUserById===========================
    public User findOne(int id){
        Optional<User> foundUser = Optional.ofNullable(usersRepository.findById(id));

        return foundUser.orElse(new User());}

//================================================================

    public List<User> findAll(){return usersRepository.findAll();}

//==========================saveUser=============================
    @Transactional
    public void save(User user){usersRepository.save(user);}
//===============================================================
    @Transactional
    public void update(int id, User updateUser){
        updateUser.setId(id);
        usersRepository.update(updateUser);
    }
//===============================================================
    @Transactional
    public void delete(int id){
        usersRepository.deleteById(id);
    }

//===============================================================
}
