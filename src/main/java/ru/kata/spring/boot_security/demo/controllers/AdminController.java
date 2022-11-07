package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;


import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private  final UserValidator userValidator;
    private  final UserService userService;
    private  RoleService roleService;
    @Autowired
    public AdminController(UserService userService, UserValidator userValidator, RoleService roleService) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.roleService = roleService;

    }


//                  ВСЕ ЮЗЕРЫ

    @GetMapping()
    public String index(Model model,@AuthenticationPrincipal User currentUser) {
        model.addAttribute("createUser", new User());
        model.addAttribute("users", userService.findAll());

        model.addAttribute("currentUser", currentUser);
        return "admin";
    }


//           ЮЗЕР СО ВСЕМИ ПОЛЯМИ


    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model){

        model.addAttribute("user", userService.findOne(id));
        return "admin";
    }


//           РЕДАКТИРОВАНИЯ ЮЗЕРА


    @GetMapping("/{id}/edit")
    public String edit( @PathVariable("id") int id, Model model){

        model.addAttribute("user", userService.findOne(id));
        return "admin";
    }



//            СТРАНИЦА С СОЗДАНИЕМ НОВОГО ЮЗЕРА


   @PostMapping("/new")
    public String newUser(@ModelAttribute("user") User user){  // @ModelAttribute помещает user без параметров

        userService.save(user);
        return "redirect:/admin";
    }



//                NEW USER

    @PostMapping()
    public String create(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult){

        userValidator.validate(user, bindingResult);

        if(bindingResult.hasErrors())
            return "admin";

        userService.save(user);
        return "redirect:/admin";
    }


//                    ИЗМЕНИТЬ ЮЗЕРА

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                         @PathVariable("id") int id){

        if(bindingResult.hasErrors())
            return "admin";

        userService.update(id, user);
        return "redirect:/admin";
    }



//                   УДАЛЕНИЕ ЮЗЕРА

   @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id, Model model) {

        model.addAttribute("user", userService.findOne(id));
        userService.delete(id);
        return "redirect:/admin";
    }


}