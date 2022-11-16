package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.dto.RoleDTO;
import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;

public interface RoleService {

        List<Role> findAllById(List<Integer> ids);

        Role findRoleById(int id);

        List<Role> findAll();

        RoleDTO convertToDto(Role role);


}
