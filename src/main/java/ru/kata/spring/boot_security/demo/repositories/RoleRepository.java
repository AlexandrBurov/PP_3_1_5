package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.Role;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class RoleRepository{

    private final EntityManager entityManager;

    @Autowired
    public RoleRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public void addRole(Role role) {
        entityManager.persist(role);
    }

    public List<Role> getAllRoles() {
        return entityManager.createQuery("select r from Role r ", Role.class)
                .getResultList();
    }

    public Role getRoleById(Long id) {
        return entityManager.find(Role.class, id);
    }

    public Role getRoleByName(String name) {
        return entityManager.find(Role.class, name);
    }



//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}
