package ru.kata.spring.boot_security.demo.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserGlobalExceptionHandler {

    //@ExceptionHandler - метод ответственный за обработку исключений.

    //Юзер не найден
    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotFoundException e){
        UserErrorResponse response = new UserErrorResponse();
        response.setMessage("User with this id wasn't found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    //Юзер не создан
    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotCreatedException e){
        UserErrorResponse response = new UserErrorResponse();
        response.setMessage("Error: User not created, "+e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //Все неверные запросы
    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(Exception e){
        UserErrorResponse response = new UserErrorResponse();
        response.setMessage(e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

//end
}
