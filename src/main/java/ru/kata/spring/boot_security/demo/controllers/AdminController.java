package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;


import javax.validation.Valid;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserValidator userValidator;

    private final UserService userService;
    @Autowired
    public AdminController(UserService userService, UserValidator userValidator) {
        this.userService = userService;


        this.userValidator = userValidator;
    }

//=====================@GetMapping==================================

    //            СТАРТОВАЯ СТРАНИЦА СО ВСЕМИ ЮЗЕРАМИ
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin-page";
    }
//==================================================================

//          ОТОБРАЖАЕТСЯ ЮЗЕР СО ВСЕМИ ПОЛЯМИ И КНОПКОЙ УДАЛЕНИЯ

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model){
        model.addAttribute("user", userService.findOne(id));
        return "delete-user";
    }
//==================================================================

//           СТРАНИЦА С СОЗДАНИЕМ НОВОГО ЮЗЕРА

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user){  // @ModelAttribute помещает user без параметров

        return "add-new-user";}
//==================================================================

    //           СТРАНИЦА ДЛЯ РЕДАКТИРОВАНИЯ ЮЗЕРА
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("user", userService.findOne(id));
        return "edit-user";
    }

//====================@PostMapping===================================

    //               СОХРАНЕНИЕ ИЗМЕНЕНИЙ ЮЗЕРА
    @PostMapping()
    public String create(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult){

        userValidator.validate(user, bindingResult);

        if(bindingResult.hasErrors())
            return "add-new-user";

        userService.save(user);
        return "redirect:/admin";         // REDIRECT переводит на нужную страницу
    }
//====================@PatchMapping============================

    //                  ОБНОВЛЯЕМ ПОЛЯ ЮЗЕРА
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                         @PathVariable("id") int id){

        userValidator.validate(user, bindingResult);

        if(bindingResult.hasErrors())
            return "edit-user";

        userService.update(id, user);
        return "redirect:/admin";
    }
//====================@DeleteMapping============================

    //                   УДАЛЕНИЕ ЮЗЕРА
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/admin";
    }

//==============================================================
}