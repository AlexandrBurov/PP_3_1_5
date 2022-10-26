package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;


import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;


@Repository
public class UsersRepository {
    private final EntityManager entityManager;

    @Autowired
    public UsersRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    //=====================================================
    public User findByUsername(String username) {
        String hql = "SELECT u FROM User u JOIN FETCH u.roles WHERE u.name = :username";

        return entityManager.createQuery(hql, User.class)
                .setParameter("username", username)
                .getSingleResult();
    }
    //=====================================================
    @Transactional
    public void save(User user) {
        entityManager.persist(user);
    }

    //=====================================================
    @Transactional
    public User update(User user) {
        return entityManager.merge(user);
    }
    //=====================================================
    public List<User> findAll() {
        String jpql = "SELECT u FROM User u";
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);

        return query.getResultList();
    }
    //=====================================================
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
}
