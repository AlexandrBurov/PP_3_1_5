package ru.kata.spring.boot_security.demo.models;

import org.springframework.security.core.GrantedAuthority;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private  int id;

    @Column(name = "name")
    private  String name;

    @Transient
    @ManyToMany(mappedBy = "roles")
    private List <User> users;


//==================================================================
    public Role(){}

    public Role(int id) {
        this.id = id;
    }

    public Role(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Role(List <User> users) {
        this.users = users;
    }

//==================================================================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//=====================Set<User>=============================
    public List <User> getUsers() {
        return users;
    }

    public void setUsers(List <User> users) {
        this.users = users;
    }

    public void addUserToRole(User user) {

        if(users == null) {

            users = new ArrayList<>();
        }

        users.add(user);
    }

//=====================GrantedAuthority=============================
    @Override
    public String getAuthority() {
        return getName();
}

//==================================================================
    @Override
    public String toString() {
        return getName();
    }

//==================================================================
}
