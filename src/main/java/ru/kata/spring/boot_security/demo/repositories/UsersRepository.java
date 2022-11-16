package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;


@Repository
public class UsersRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    private final RoleRepository roleRepository;

    public PasswordEncoder bCryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Lazy
    public UsersRepository(EntityManager entityManager, RoleRepository roleRepository) {
        this.entityManager = entityManager;
        this.roleRepository = roleRepository;
    }


    public User findByUsername(String username) {
        return entityManager.createQuery
                        ("select u FROM User u JOIn fETCH u.roles WHERe u.username = :username", User.class)
                .setParameter("username", username)
                .getResultList().stream().findAny().orElse(null);
    }

    @Transactional
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder().encode(user.getPassword()));
        entityManager.persist(user);
    }

    @Transactional
    public User update(User user1) {
        User user2 = findById(user1.getId());
        if(!user2.getPassword().equals(user1.getPassword())) {
            user1.setPassword(bCryptPasswordEncoder().encode(user2.getPassword()));
        }
        return entityManager.merge(user1);
    }

    public List<User> findAll() {
        return entityManager.createQuery("select s from User s", User.class).getResultList();
    }

    public User findById(int id) {
        Optional<User> foundUser = Optional.ofNullable(entityManager.find(User.class, id));
        return foundUser.orElse(null);
    }

    @Transactional
    public void deleteById(int contactId) {
        User user = entityManager.find(User.class, contactId);
        entityManager.remove(user);
    }

// Optional

    public Optional<User> getUserByEmail (String email) {

        TypedQuery<User> query = entityManager.createQuery
                ("SELECT u FROM User u WHERE u.email = :email", User.class);

        query.setParameter("email", email);

        return query.getResultStream().findAny();

    }

    public Optional<User> findUserOptional(String username) {

    TypedQuery<User> query = entityManager.createQuery
                    ("SELECT u FROM User u WHERE u.username = :username", User.class);

    query.setParameter("username", username);

    return query.getResultStream().findAny();

    }

// close
}