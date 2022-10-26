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

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

//=====================ПЕРЕКИДЫВАЕТ ЮЗЕРОВ ПО НУЖНЫМ АДРЕСАМ ПОСЛЕ ВХОДА=========================================
    private final SuccessUserHandler successUserHandler;

    public WebSecurityConfig(SuccessUserHandler successUserHandler) {

        this.successUserHandler = successUserHandler;
    }
//==========================Шифрование паролей====================================
    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }


//==================== Конфигурируем Spring Security Авторизацию =========================================

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()//все запросы будут проходить через авторизацию
                .antMatchers("/", "/index").permitAll()//на эти страницы пускаем всех
                //.antMatchers("/admin/**").hasRole("ADMIN")//ВХОД ТОЛЬКО АДМИНУ
                .anyRequest().authenticated()//на все остальные запросы нужна авторизация
                .and()// and соединяет различающиеся по логике настройки, следом настраивается страница логина
                .formLogin().successHandler(successUserHandler)//УКАЗЫВАЕМ НАПРАВЛЕНИЕ ПОСЛЕ РЕГИСТРАЦИИ
                .permitAll()
                .and()
                .logout() //.logoutSuccessUrl("/") //после logout переводит в корень
                .permitAll();
    }

//============СВЕРЯЕТ ДАННЫЕ, отдаем ему логины и пароли, его задача сказать существует ли такой юзер или нет.
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());  // расшифровка паролей, преобр к хэшу и сравнивает
        authenticationProvider.setUserDetailsService(userService);    //по имени юзера достает его из базы


        return authenticationProvider;
    }



//==============================================================
}