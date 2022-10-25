package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

//WebSecurityConfig - ГЛАВНЫЙ КЛАСС ГДЕ НАСТРАИВАЕТСЯ SPRING SECURITY.
//Происходит настройка секьюрности по определенным URL,
// а также настройка UserDetails и GrantedAuthority.
@Configuration// не обязательно указывать
@EnableWebSecurity  //Аннотация дает понять что это конфигурационный класс для SPRING SECURITY.
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;

    public WebSecurityConfig(SuccessUserHandler successUserHandler) {
        this.successUserHandler = successUserHandler;
    }
//<<<<<<<<<<<<<<<<<<<<configure>>>>>>>>>>>>>>>>>>>>>>>
    @Override// конфигурируем сам Spring Security Авторизацию
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()//запрашивается авторизация для определенных URL
                .antMatchers("/", "/index").permitAll()//кто может видеть страницу .hasAnyRole()
                .anyRequest().authenticated()//на все остальные запросы нужна авторизация
                .and()// and соединяет различающиеся по логике настройки, следом настраивается страница логина
                .formLogin().successHandler(successUserHandler)//ЛОГИН И ПАРОЛЬ ЗАПРАШИВАЕТСЯ У ВСЕХ
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    // аутентификация inMemory
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("user")
                        .roles("USER")
                        .build();
//Измените настройку секьюрности с inMemory на userDetailService.
        return new InMemoryUserDetailsManager(user);
    }
}