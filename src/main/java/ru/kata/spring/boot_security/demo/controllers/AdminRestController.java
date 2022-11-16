package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.RoleDTO;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/admin2")
public class AdminRestController {

    private final UserService userService;
    private  final UserValidator userValidator;
    private final RoleService roleService;

    @Autowired
    public AdminRestController(UserService userService, UserValidator userValidator, RoleService roleService) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.roleService = roleService;
    }


//  Получаем всех юзеров
    @GetMapping()
    public ResponseEntity<List<UserDTO>> getAllUsers() {

        List<UserDTO> userDTOList = userService.findAll().stream()
                .map(userService::convertToUserDTO).collect(Collectors.toList());

        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }


//  Получаем юзера по id, @PathVariable - получение значения переменной из адресса запроса.
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getOneUser(@PathVariable("id") int id) {

        UserDTO userDTO = userService.convertToUserDTO(userService.findOne(id));
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }


//  Создаем юзера
    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody UserDTO userDTO,
                                             BindingResult bindingResult) {

        User user = userService.convertToUser(userDTO);
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {

              StringBuilder errorMsg = new StringBuilder();
              List<FieldError> errors = bindingResult.getFieldErrors();
              for ( FieldError error : errors) {
                  errorMsg.append(error.getField())
                          .append(" - ").append(error.getDefaultMessage())
                          .append(";");
              }
              throw new UserNotCreatedException(errorMsg.toString());
        }
        userService.save(userService.convertToUser(userDTO));

        return ResponseEntity.ok(HttpStatus.CREATED);
    }

//  Редактируем юзера
    @PatchMapping("{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody UserDTO userDTO, @PathVariable("id") int id) {

        userService.update(id, userService.convertToUser(userDTO));
        return ResponseEntity.ok(HttpStatus.UPGRADE_REQUIRED);
}

//  Удаляем юзера
    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {

        User user = userService.findOne(id);
        if(user == null) {
            throw new UsernameNotFoundException("There is no user with ID = " +id+ "in Database");
        }
        userService.delete(id);

        return ResponseEntity.ok(HttpStatus.OK);
    }

//-------------------------
    @GetMapping("/roles")
    public ResponseEntity<List<RoleDTO>> showAllRoles() {
        List<RoleDTO> roleDTOList = roleService.findAll().stream()
                .map(roleService::convertToDto).collect(Collectors.toList());

        return new ResponseEntity<>(roleDTOList, HttpStatus.OK);
    }

    @GetMapping("/roles/{id}")
    ResponseEntity<RoleDTO> getRoleById(@PathVariable("id") int id){
        return new ResponseEntity<>(roleService.convertToDto(roleService.findRoleById(id)), HttpStatus.OK);
    }


}
