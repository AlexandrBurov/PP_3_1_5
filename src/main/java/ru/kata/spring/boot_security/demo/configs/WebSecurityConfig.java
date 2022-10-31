package ru.kata.spring.boot_security.demo.configs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//<<<<<<<<<<<<<<<<<<<<<<<ГЛАВНЫЙ КЛАСС ГДЕ НАСТРАИВАЕТСЯ SPRING SECURITY.>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

//Настройка секьюрности по определенным URL, настройка UserDetails и GrantedAuthority

@Configuration
@EnableWebSecurity  //Аннотация дает понять что это конфигурационный класс для SPRING SECURITY.
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {// Настраиваем части безопасности


//             ПЕРЕКИДЫВАЕТ ЮЗЕРОВ ПО НУЖНЫМ АДРЕСАМ ПОСЛЕ ВХОДА


    private final SuccessUserHandler successUserHandler;
    private final UserDetailsService userDetailsService;
    @Autowired
    public WebSecurityConfig(SuccessUserHandler successUserHandler, UserDetailsService userDetailsService) {
        this.successUserHandler = successUserHandler;
        this.userDetailsService = userDetailsService;
    }
//                          ШИФРОВАНИЕ ПАРОЛЕЙ


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

//                   Конфигурируем Spring Security Авторизацию

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
                .logout().permitAll()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login");

    }

//             СВЕРЯЕТ ДАННЫЕ, проверяет по логину и паролю существует ли такой пользователь

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {  //проверяет по логину и паролю существует ли такой пользователь
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);  //Предоставляет пользователей из userService (по имени пользователя)
        return provider;
    }
//==============================================================
}