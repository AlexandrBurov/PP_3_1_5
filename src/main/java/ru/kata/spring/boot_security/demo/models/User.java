package ru.kata.spring.boot_security.demo.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "users")
public class User implements UserDetails{
    //=========================================
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //----------------------------------------
    @Column(name = "firstname")
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String firstname;

    //----------------------------------------
    @Column(name = "lastname")
    @NotEmpty(message = "Surname should not be empty")
    @Size(min = 2, max = 30, message = "Surname should be between 2 and 30 characters")
    private String lastname;

    //----------------------------------------
    @Column(name = "age")
    @Min(value = 0, message = "Age should be greater than 0")
    private int age;

    //=========================================

    @Column(name = "email")
    @NotEmpty(message = "email should not be empty")
    @Email(message = "Email should be valid")
    private String email;

//==================   USERNAME AND PASSWORD   =============================

    @Column(name = "username")
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String username;

    @Column(name = "password")
    private String password;

//======================================================================

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List <Role> roles;

//====================================================================

    public User(){}

    public User(int id, String firstname, String lastname, int age, String email, String username,String password) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.email = email;


        this.username = username;
        this.password = password;

    }

    //===========================================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//=============================================

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//====================ROLE====================
    public List <Role> getRoles() {
        return roles;
    }

    public void setRoles(List <Role> roles) {
        this.roles = roles;
    }

    public void addRoleToUser(Role role) {

        if(roles == null) {

            roles = new ArrayList<>();
        }

        roles.add(role);
    }

//<<<<<<<<<<<<<<<<<<<<UserDetails>>>>>>>>>>>>>>>>>>>>

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return getRoles();
    }

    @Override
    public String getUsername() {

        return username;
    }

    public String getPassword() {

        return password;
    }
//<<<<<<<<<<<<<<<<<<<<UserDetails_true>>>>>>>>>>>>>>>>>>>>

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

//=========================================================
}
