package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import ru.kata.spring.boot_security.demo.services.UserService;

//<<<<<<<<<<<<<<<<<<<<<<<ГЛАВНЫЙ КЛАСС ГДЕ НАСТРАИВАЕТСЯ SPRING SECURITY.>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

//Происходит настройка секьюрности по определенным URL,
// а также настройка UserDetails и GrantedAuthority.


@Configuration
@EnableWebSecurity  //Аннотация дает понять что это конфигурационный класс для SPRING SECURITY.
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {// Настраиваем части безопасности


//=====================ПЕРЕКИДЫВАЕТ ЮЗЕРОВ ПО НУЖНЫМ АДРЕСАМ ПОСЛЕ ВХОДА=========================================
    private final UserService userService;
    private final SuccessUserHandler successUserHandler;

    public WebSecurityConfig(SuccessUserHandler successUserHandler, UserService userService) {

        this.successUserHandler = successUserHandler;

        this.userService = userService;
    }
//==========================Шифрование паролей====================================
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//==================== Конфигурируем Spring Security Авторизацию =========================================

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http

                .authorizeRequests()
                .antMatchers("/").authenticated()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .and()
                .formLogin().successHandler(successUserHandler)//УКАЗЫВАЕМ НАПРАВЛЕНИЕ ПОСЛЕ РЕГИСТРАЦИИ
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

//============СВЕРЯЕТ ДАННЫЕ, проверяет по логину и паролю существует ли такой пользователь ============

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {  //проверяет по логину и паролю существует ли такой пользователь
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userService);  //Предоставляет пользователей из userService (по имени пользователя)
        return provider;
    }



//==============================================================
}