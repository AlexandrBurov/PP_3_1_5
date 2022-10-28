package ru.kata.spring.boot_security.demo.repositories;


import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;



@Repository
@Transactional(readOnly = true)
public class UsersRepository {
    @PersistenceContext
    private final EntityManager entityManager;
    private RoleRepository roleRepository;

    @Lazy
    public UsersRepository(EntityManager entityManager, RoleRepository roleRepository) {
        this.entityManager = entityManager;
        this.roleRepository = roleRepository;
    }
//====================================================
    @Transactional
    public User findByUsername(String username) throws UsernameNotFoundException  {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :name", User.class);
        query.setParameter("name", username);
        if (username == null) {
            throw new UsernameNotFoundException("User not found");
        }
        User user = query.getSingleResult();
        user.getRoles().size();
        return user;
    }
//=====================================================
    @Transactional
    public void save(User user) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        Role roleUser = findRoleByRoleName("ROLE_USER");
        user.addRoleToUser(roleUser);
        entityManager.persist(user);
    }
//=====================================================
    @Transactional
    public User update(User user) {

        return entityManager.merge(user);
    }
//=========================getAllUsers=================
    public List<User> findAll() {
        String jpql = "SELECT u FROM User u";

        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);

        return query.getResultList();
    }
//=======================getUserById===================
    public User findById(int id) {

        return entityManager.find(User.class, id);
    }
//=====================================================
    @Transactional
    public void deleteById(int contactId) {
        User user = entityManager.find(User.class, contactId);
        entityManager.remove(user);
    }
//=====================================================
    public Role findRoleByRoleName(String name) {
        TypedQuery<Role> query = entityManager.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }
//=====================================================
}