package com.example.demo.controller;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandleController {
    @ExceptionHandler(UnauthorizedException.class)
    public String handleShiroException() {
        return "redirect:/403";
    }
}
